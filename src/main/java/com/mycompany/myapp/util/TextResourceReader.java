package com.mycompany.myapp.util;

import android.content.*;
import java.io.*;
import android.content.res.*;

public class TextResourceReader
{
	public static String readTextFileFromResource(Context context,int resourceId)
	{
		StringBuilder body=new StringBuilder();
		try
		{
			InputStream inputStream=context.getResources().openRawResource(resourceId);
			InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
			BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
			String nextLine;
			while((nextLine=bufferedReader.readLine()) !=null)
			{
				body.append(nextLine);
				body.append('\n');
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not open resource: " + resourceId,e);
		}
		catch (Resources.NotFoundException nfe)
		{
			throw new RuntimeException("Resource not found: " + resourceId,nfe);
		}
		return body.toString();

    }
}
