package com.github.igotyou.FactoryMod.recipes;

import com.github.igotyou.FactoryMod.FactoryMod;
import com.github.igotyou.FactoryMod.factories.FurnCraftChestFactory;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
import vg.civcraft.mc.civmodcore.itemHandling.ItemMap;

import java.util.LinkedList;
import java.util.List;

public class FreeFormRecipe extends InputRecipe {
	private List<Material> availableMaterials;
	private ItemStack baseOutput;
	private ItemStack recipeRepresentation;


	public FreeFormRecipe(String identifier,
						  ItemMap input,
						  List<Material> availableMaterial,
						  ItemStack baseOutput,
						  ItemStack recipeRepresentation,
						  String name,
						  int productionTime,
						  int minInputs)
	{
		super(identifier, name, productionTime, input);
		this.baseOutput = baseOutput;
		this.availableMaterials = availableMaterial;
		this.recipeRepresentation = recipeRepresentation != null ? recipeRepresentation : new ItemStack(Material.STONE);
	}

	public List<Material> getAvailableMaterials() {
		return availableMaterials;
	}

	@Override
	public List<ItemStack> getInputRepresentation(Inventory i, FurnCraftChestFactory fccf) {
		List<ItemStack> result = new LinkedList<>();

		for (Material m : availableMaterials) {
			result.add(new ItemStack(m));
			if (result.size() > 14) {
				break;
			}
		}
		ItemStack moreitems = new ItemStack(Material.KNOWLEDGE_BOOK, 64);
		ItemAPI.addLore(moreitems,"Plus other plants");
		result.add(moreitems);
		return result;
	}

	@Override
	public List<ItemStack> getOutputRepresentation(Inventory i, FurnCraftChestFactory fccf) {
		List<ItemStack> result = new LinkedList<>();
		result.add(new ItemStack(baseOutput.getType()));
		return result;
	}

	@Override
	public ItemStack getRecipeRepresentation() {
		ItemAPI.setDisplayName(this.recipeRepresentation, getName());
		return this.recipeRepresentation.clone();
	}

	@Override
	public void applyEffect(Inventory i, FurnCraftChestFactory fccf) {
		logBeforeRecipeRun(i, fccf);
		//TODO : rewrite to run as single batch (necessary to include variation weights)
		for (ItemStack is : i.getContents()) {
			ItemMap im = new ItemMap(i);
			if (is != null) {
				if (availableMaterials.contains(is.getType())) {
					i.remove(is);
					i.addItem(baseOutput);
				}
			}
		}
	}


	/***
	 * Only checks that there are some available stew inputs in the factory;
	 * checking if stew can actually be made would give away item value at no cost to player.
	 */
	public boolean enoughMaterialAvailable(Inventory i) {
		ItemMap im = new ItemMap(i);
		for (ItemStack is : i.getContents()) {
			if (is != null) {
				if (availableMaterials.contains(is.getType())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getTypeIdentifier() {
		return "FREEFORM";
	}
}
