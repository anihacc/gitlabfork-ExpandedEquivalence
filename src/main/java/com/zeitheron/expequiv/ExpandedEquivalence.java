package com.zeitheron.expequiv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pengu.hammercore.HammerCore;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;

@Mod(modid = InfoEE.MOD_ID, name = InfoEE.MOD_NAME, version = InfoEE.MOD_VERSION, certificateFingerprint = "4d7b29cd19124e986da685107d16ce4b49bc0a97", dependencies = "required-after:hammercore;required-after:projecte")
public class ExpandedEquivalence
{
	public static final Logger LOG = LogManager.getLogger(InfoEE.MOD_ID);
	
	@EventHandler
	public void certificateViolation(FMLFingerprintViolationEvent e)
	{
		LOG.warn("*****************************");
		LOG.warn("WARNING: Somebody has been tampering with ExpandedEquivalence jar!");
		LOG.warn("It is highly recommended that you redownload mod from https://minecraft.curseforge.com/projects/295222 !");
		LOG.warn("*****************************");
		HammerCore.invalidCertificates.put(InfoEE.MOD_ID, "https://minecraft.curseforge.com/projects/295222");
	}
}