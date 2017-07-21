package at.xander.unifiedstone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraftforge.oredict.OreDictionary;

public class MyCfg
{
  private final int maxCharsPerLine = 120;
  private final ArrayList<String> startBlocks = new ArrayList();
  private File cfg;
  
  public MyCfg(File cfg)
  {
    this.startBlocks.add("minecraft:cobblestone");
    this.startBlocks.add("minecraft:stone");
    this.startBlocks.add("minecraft:stonebrick");
    this.startBlocks.add("minecraft:netherrack");
    this.startBlocks.add("minecraft:end_stone");
    this.startBlocks.add("minecraft:mossy_cobblestone");
    this.startBlocks.add("minecraft:stonebrick:1");
    for (int i = 1; i <= 6; i++) {
      this.startBlocks.add("minecraft:stone:" + i);
    }
    this.cfg = cfg;
  }
  
  public void createCfgIfNotPresent()
    throws IOException
  {
    if (!this.cfg.exists())
    {
      this.cfg.createNewFile();
      BufferedWriter writer = new BufferedWriter(new FileWriter(this.cfg));
      writer.write("# This Config File is a List of all Blocks that can be used to craft stone tools.\n");
      writer.write("# You can add and remove blocks here\n");
      writer.write("# A Comment Line starts with a # and is ignored when parsing\n");
      writer.write("# All block names have to be in the Format ModID:BlockName:MetaData, \n");
      writer.write("# If the MetaData is 0, you don't have to write it\n");
      writer.write("# For example the Abyssal Cobblestone from Railcraft has the ID\n");
      writer.write("# Railcraft:brick.abyssal:5\n");
      writer.write("# All the Block names have to be seperated by \",\"\n");
      writer.write(getDefaultBlocks());
      writer.close();
    }
  }
  
  public void readConfig()
    throws IOException
  {
    BufferedReader reader = new BufferedReader(new FileReader(this.cfg));
    LineIterator it = new LineIterator(reader);
    StringBuilder wholeString = new StringBuilder();
    for (String line : it) {
      if (line.charAt(0) != '#') {
        wholeString.append(line.replace(System.lineSeparator(), ""));
      }
    }
    addItems(wholeString.toString());
    reader.close();
  }
  
  private String getDefaultBlocks()
  {
    StringBuilder defaultBlocks = new StringBuilder();
    int chars = 0;
    for (String block : this.startBlocks)
    {
      chars += block.length() + 2;
      defaultBlocks.append(block + ", ");
      if (chars >= 120)
      {
        defaultBlocks.append(System.lineSeparator());
        chars = 0;
      }
    }
    String s = defaultBlocks.toString();
    s = s.substring(0, s.lastIndexOf(","));
    return s;
  }
  
  private void addItems(String items)
  {
    String[] seperate = items.split(",");
    ArrayList<String> noItem = null;
    for (String item : seperate) {
      try
      {
        OreDictionary.registerOre("listAllStone", getItemStackFromString(item));
      }
      catch (NoSuchObjectException e)
      {
        if (noItem == null) {
          noItem = new ArrayList();
        }
        noItem.add(item);
      }
    }
    if (noItem != null) {
      for (Iterator<String> iter = noItem.iterator(); iter.hasNext();)
      {
        String errorItem = iter.next();
        System.err.println(errorItem + " does not exist and will be ignored in Config");
      }
    }
  }
  
  private ItemStack getItemStackFromString(String itemName)
    throws NoSuchObjectException
  {
    String[] data = itemName.split(":");
    data = trimAll(data);
    
    ResourceLocation location = new ResourceLocation(data[0], data[1]);
    Block block = (Block)Block.REGISTRY.getObject(location);
    ItemStack stack;
    if (block != null)
    {
      stack = new ItemStack(block, 1, data.length == 3 ? Integer.parseInt(data[2]) : 0);
    }
    else
    {
      Item item = (Item)Item.REGISTRY.getObject(location);
      if (item != null) {
        stack = new ItemStack(item, 1, data.length == 3 ? Integer.parseInt(data[2]) : 0);
      } else {
        throw new NoSuchObjectException("Item Specified by String Name does not exist");
      }
    }
    return stack;
  }
  
  private String[] trimAll(String[] string)
  {
    String[] trimmed = new String[string.length];
    for (int i = 0; i < string.length; i++) {
      trimmed[i] = string[i].trim();
    }
    return trimmed;
  }
}
