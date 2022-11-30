package alloyfek.fusemod.proxy;

import alloyfek.fusemod.lists.Blocks;
import alloyfek.fusemod.lists.Items;
import alloyfek.fusemod.register.EventRegister;

public class CommonProxy {
	public void registerPre() {
		EventRegister.register();
		// EnchantmentRegister.register();
		Blocks.register();
		Items.register();
	}

	public void register() {
	}
}