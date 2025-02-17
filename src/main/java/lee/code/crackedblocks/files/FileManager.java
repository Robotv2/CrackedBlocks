package lee.code.crackedblocks.files;

import lee.code.crackedblocks.CrackedBlocks;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@RequiredArgsConstructor
public class FileManager {

    private final ConcurrentHashMap<String, CustomFile> configs = new ConcurrentHashMap<>();

    public void addConfig(String name) {
        CrackedBlocks plugin = CrackedBlocks.getPlugin();
        configs.put(name, new CustomFile(name + ".yml", "", plugin.getResource(name + ".yml"), plugin));
    }

    public void addConfig(String name, String path) {
        CrackedBlocks plugin = CrackedBlocks.getPlugin();
        configs.put(name, new CustomFile(name + ".yml", path, plugin.getResource(name + ".yml"), plugin));
    }

    public CustomFile getConfig(String name) {
        return configs.get(name);
    }

    public void reloadAll() {
        configs.values().forEach(CustomFile::reload);
    }

    public void loadConfigFolder(String path) {
        CrackedBlocks plugin = CrackedBlocks.getPlugin();
        final File folder = new File(path);
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.getName().endsWith(".yml")) {
                try {
                    FileInputStream inputStream = new FileInputStream(file);
                    String name = file.getName().replace(".yml", "");
                    configs.put(name, new CustomFile(file.getName(), "", inputStream, plugin));
                } catch (FileNotFoundException ignored) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to load configuration file: " + file.getName());
                }
            }
        }
    }
}