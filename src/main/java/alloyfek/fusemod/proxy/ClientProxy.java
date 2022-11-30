package alloyfek.fusemod.proxy;

import alloyfek.fusemod.register.ModelRegister;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerPre() {
		super.registerPre();
		ModelRegister.register();
	}

	@Override
	public void register() {}
}