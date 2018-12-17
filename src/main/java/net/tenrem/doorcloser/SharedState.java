package net.tenrem.doorcloser;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.logging.Logger;

public class SharedState 
{
    public static boolean WorldGuardIntegrationEnabled = false;

    public static Settings Settings;

    public SharedState()
    {
        // TODO: Make Settings a singleton hanging off this class

        // TODO: Make logger and Plugin static members of this class
    }


    
}