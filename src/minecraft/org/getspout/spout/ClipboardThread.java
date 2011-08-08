package org.getspout.spout;
//Spout
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import net.minecraft.src.EntityClientPlayerMP;

import org.getspout.spout.packet.*;

public class ClipboardThread extends Thread {
	public ClipboardThread(EntityClientPlayerMP player) {
		this.player = player;
	}
	EntityClientPlayerMP player;
	String prevClipboardText = "";
	public void run() {
		while(!isInterrupted()) {
			try {
				sleep(1000);
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}
			try {
				Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
				if (contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					String text = null;
					try {
						text = (String) contents.getTransferData(DataFlavor.stringFlavor);
					} catch (UnsupportedFlavorException e) {

					} catch (IOException e) {

					}
					if (text != null) {
						if (!text.equals(prevClipboardText)) {
							prevClipboardText = text;
							player.sendQueue.addToSendQueue(new CustomPacket(new PacketClipboardText(text)));
						}
					}
				}
			}
			catch (Exception e2) {
			
			}
		}
	}

}
