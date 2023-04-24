package io.github.adainish.cobbledjobsfabric.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.cobbledjobsfabric.CobbledJobsFabric;
import io.github.adainish.cobbledjobsfabric.obj.configurabledata.JobManager;
import io.github.adainish.cobbledjobsfabric.util.Adapters;

import java.io.*;

public class JobsConfig
{
    public JobManager jobManager;

    public JobsConfig()
    {
        jobManager = new JobManager();
        jobManager.initDefaults();

    }


    public static void writeConfig()
    {
        File dir = CobbledJobsFabric.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        JobsConfig config = new JobsConfig();
        try {
            File file = new File(dir, "jobs.json");
            if (file.exists())
                return;
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            String json = gson.toJson(config);
            writer.write(json);
            writer.close();
        } catch (IOException e)
        {
            CobbledJobsFabric.getLog().warn(e);
        }
    }

    public static JobsConfig getConfig()
    {
        File dir = CobbledJobsFabric.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        File file = new File(dir, "jobs.json");
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            CobbledJobsFabric.getLog().error("Something went wrong attempting to read the Jobs Config");
            return null;
        }

        return gson.fromJson(reader, JobsConfig.class);
    }
}
