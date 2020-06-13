package tk.zeitheron.expequiv.api.js;

import com.google.common.base.Strings;
import com.zeitheron.hammercore.lib.zlib.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.function.BiPredicate;

public interface JSSource
{
	String read();
	
	default boolean exists()
	{
		return !Strings.isNullOrEmpty(read());
	}
	
	default JSSource processImports()
	{
		final JSSource main = this;
		return new JSSource()
		{
			@Override
			public String read()
			{
				StringBuilder ns = new StringBuilder();
				String script = main.read();
				try(Scanner scan = new Scanner(script))
				{
					while(scan.hasNextLine())
					{
						String ln = scan.nextLine();
						String trimmed = ln.trim();
						if(trimmed.startsWith("import "))
						{
							if(trimmed.endsWith(";")) ln = trimmed.substring(7, trimmed.length() - 1);
							else ln = trimmed.substring(7);
							String simpleName = ln.substring(ln.lastIndexOf('.') + 1);
							ln = "var " + simpleName + " = Java.type(\"" + ln + "\");";
						}
						ns.append(ln).append(scan.hasNextLine() ? "\n" : "");
					}
				}
				return ns.toString();
			}
			
			@Override
			public boolean exists()
			{
				return main.exists();
			}
		};
	}
	
	default JSSource addClassPointer(Class<?> type, String reference)
	{
		final JSSource main = this;
		return new JSSource()
		{
			@Override
			public String read()
			{
				return "var " + reference + " = Java.type(\"" + type.getCanonicalName() + "\");\n" + main.read();
			}
			
			@Override
			public boolean exists()
			{
				return main.exists();
			}
		};
	}
	
	default JSSource inheritClassMethods(Class<?> type)
	{
		return inheritClassMethods(type, JSLocalizer.alwaysTrue);
	}
	
	default JSSource inheritClassMethods(Class<?> type, BiPredicate<String, Method> allow)
	{
		final JSSource main = this;
		return new JSSource()
		{
			@Override
			public String read()
			{
				return JSLocalizer.createJSWrap(type, allow) + main.read();
			}
			
			@Override
			public boolean exists()
			{
				return main.exists();
			}
		};
	}
	
	static JSSource fromLocalResource(String path)
	{
		String fp = path.startsWith("/") ? path : "/" + path;
		return () ->
		{
			byte[] data = new byte[0];
			try(InputStream in = JSSource.class.getResourceAsStream(fp))
			{
				if(in != null) data = IOUtils.pipeOut(in);
			} catch(IOException e)
			{
			}
			return data.length == 0 ? null : new String(data);
		};
	}
}