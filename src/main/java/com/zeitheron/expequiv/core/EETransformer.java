package com.zeitheron.expequiv.core;

import java.lang.reflect.Field;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class EETransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(name.equals("moze_intel.projecte.emc.mappers.SmeltingMapper"))
			basicClass = transformSmeltingMapper(basicClass);
		return basicClass;
	}
	
	private static String getOpcodeName(int op)
	{
		for(Field f : Opcodes.class.getDeclaredFields())
		{
			int o = 0;
			try
			{
				o = f.getInt(null);
			} catch(Throwable err)
			{
			}
			if(o == op)
				return f.getName();
		}
		return "?";
	}
	
	public byte[] transformSmeltingMapper(byte[] basicClass)
	{
		System.out.println("Transforming moze_intel.projecte.emc.mappers.SmeltingMapper...");
		
		ClassNode node = ObjectWebUtils.loadClass(basicClass);
		
		for(MethodNode m : node.methods)
			if(m.name.equals("addMappings"))
			{
				System.out.println("  Inserting custom mapping...");
				InsnList i = new InsnList();
				i.add(new VarInsnNode(Opcodes.ALOAD, 1));
				i.add(new VarInsnNode(Opcodes.ALOAD, 2));
				i.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/expequiv/ExpandedEquivalence", "addMappings", m.desc, false));
				// Add custom hook
				m.instructions.insert(i);
			}
		
		return ObjectWebUtils.writeClassToByteArray(node);
	}
}