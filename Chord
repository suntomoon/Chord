import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Chord {
	/**
	 * sharedNodes is a list to store all shared nodes
	 */
	private static List<Node> sharedNodes = new ArrayList<Node>();
	
	/**
	 * bitNumder is to record the maximum number of nodes, for 32, total is 2^32
	 */
	private static int bitNumber = 32;

	/**
	 * Main function is the entrance of this program, it reads from stdin, and trigger different public functions 
	 */
	public static void main(String[] args) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in));
			String tempString = null;

			while ((tempString = reader.readLine()) != null) {
				if (tempString.startsWith("#") || tempString.trim().isEmpty()) {
					continue;
				}

				System.out.println(tempString);

				int start = tempString.indexOf("(");
				if (start != -1) {
					String name = tempString.substring(0, start).trim();
					int end = tempString.lastIndexOf(")");
					
					if(end != -1) {
						String paraString = tempString.substring(start + 1, end).trim().replaceAll("\"", "");
						
						String params[] = paraString.split(",");
						
						if(name.equals("addNode")) {
							checkParams(tempString, params, "addNode");
							
							addNode(convertIPToBigInteger(params[0]));
						} else if(name.equals("removeNode")) {
							checkParams(tempString, params, "removeNode");
							
							removeNode(convertIPToBigInteger(params[0]));
						} else if(name.equals("insertItem")) {
							checkParams(tempString, params, "insertItem");
							
							insertItem(convertIPToBigInteger(params[0]), params[1]);
						} else if(name.equals("deleteItem")) {
							checkParams(tempString, params, "deleteItem");
							
							deleteItem(convertIPToBigInteger(params[0]), params[1]);
						} else if(name.equals("find")) {
							checkParams(tempString, params, "find");
							
							find(convertIPToBigInteger(params[0]), params[1]);
						} else {
							System.out.println("Error: invalid function name \"" + name + "\"!");
							System.exit(0);
						}
					} else {
						System.out.println("Error: there is bad format at \"" + tempString + "\"!");
						System.exit(0);
					}
				} else {
					System.out.println("Error: there is bad format at \"" + tempString + "\"!");
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//printShardNodesInfo();
	}

	/***************************************
	 * 
	 * public methods
	 * 
	 *****************************************/
	
	/**
	 * addNode
	 *   add specific node as a sharing node, add it into sharing list
	 * @param ip
	 *   the IP address of specific node
	 */
	@SuppressWarnings("unchecked")
	public static void addNode(BigInteger ip) {
		//System.out.println("addNode: " + ip);
		
		if(isSharedNode(ip)) {
			System.out.println("    it has already been a shared node!");
			System.out.println("    returns failed");
			return;
		}
		
		Node shareNode = new Node(ip);
		BigInteger base = new BigInteger("2");
		BigInteger moder = base.pow(bitNumber);
		
		// build finger table
		for(int j=0; j<bitNumber; j++) {
			int index = j;
			BigInteger jThSuccessor = ip.add(base.pow(j)).mod(moder);
			
			FingerTableItem ftItem = new FingerTableItem(index, jThSuccessor, buildSuccessor(jThSuccessor, ip));
			shareNode.getFingerTable().add(ftItem);
		}
		
		
		// add new shared node into list
		sharedNodes.add(shareNode);
		Collections.sort(sharedNodes);
		
		// update successor of all shared nodes
		updateSuccessor();
		
		//printShardNodesInfo();
		
		System.out.println("    returns success");
	}

	/**
	 * removeNode
	 *   remove the specific node from sharing list
	 * @param ip
	 *   the IP address of specific node
	 */
	@SuppressWarnings("unchecked")
	public static void removeNode(BigInteger ip) {
		//System.out.println("removeNode: " + ip);
		if(!isSharedNode(ip)) {
			System.out.println("    this is not a shared node");
			System.out.println("    returns failed");
			return;
		} else {
			Node sharedNode = getSharedNode(ip);
			
			Node successor = getSharedNode(findSuccessor(ip));
			
			if(ip.compareTo(findSuccessor(ip)) != 0) {
				for(SongTableItem sti : sharedNode.getSongTable()) {
					if(sti.getIP().compareTo(ip) == 0) {
						SongTableItem newItem = new SongTableItem(sti.getTitle(), successor.getIP());
						successor.getSongTable().add(newItem);
					} else {
						SongTableItem newItem = new SongTableItem(sti.getTitle(), sti.getIP());
						successor.getSongTable().add(newItem);
					}
				}
			}
			
			sharedNodes.remove(sharedNode);
			Collections.sort(sharedNodes);
			
			// update successor of all shared nodes
			updateSuccessor();
		}
		
		//printShardNodesInfo();
		
		System.out.println("    returns success");
	}

	/**
	 * insertItem
	 *   insert a sharing item into specific node
	 * @param ip
	 *   the IP address of specific node
	 * @param title
	 *   the title of sharing item
	 */
	public static void insertItem(BigInteger ip, String title) {
		//System.out.println("insertItem:" + ip + "," + title);
		title = title.trim();
		Boolean isSuccess = false;
		
		BigInteger songTitleHash = hashMusicTitle(title);
		
		if(isSharedNode(ip)) {
			isSuccess = storeSongIntoST(ip, title, songTitleHash, ip);
			
		} else {
			BigInteger successor = findSuccessor(ip);
			
			//System.out.println(successor);
			
			isSuccess = storeSongIntoST(ip, title, songTitleHash, successor);
		}
		
		
		if(isSuccess) {
			System.out.println("    return success");
		} else {
			System.out.println("    return failed");
		}
	}

	/**
	 * find
	 *   search item from specific node
	 * @param ip
	 *   the IP address of specific node
	 * @param title
	 *   the title of searching item
	 */
	public static void find(BigInteger ip, String title) {
		//System.out.println("find:" + ip + "," + title);
		title = title.trim();
		StringBuilder sb = new StringBuilder("    " + convertBigIntegerToIP(ip));
		Boolean isExisting = false;
		
		BigInteger songTitleHash = hashMusicTitle(title);
		
		if(isSharedNode(ip)) {
			isExisting = findSongInST(ip, title, sb, isExisting, songTitleHash, ip);
		} else {
			BigInteger successor = findSuccessor(ip);
			
			String successorIP = convertBigIntegerToIP(successor);
			sb.append(" -> " + successorIP + "(successor)");
			
			isExisting = findSongInST(ip, title, sb, isExisting, songTitleHash, successor);
		}
		
		if(isExisting) {
			System.out.println("    returns success");
		} else {
			System.out.println("    returns failed");
		}
		
		System.out.println("    \"" + title + "\" hashed to " + convertBigIntegerToHex(songTitleHash) + "(" + songTitleHash + ")");
		
		System.out.println(sb.toString());
	}
	
	/**
	 * deleteItem
	 *   delete a sharing item from specific node
	 * @param ip
	 *   the IP address of specific node
	 * @param title
	 *   the title of sharing item
	 */
	public static void deleteItem(BigInteger ip, String title) {
		//System.out.println("deleteItem:" + ip + "," + title);
		title = title.trim();
		StringBuilder sb = new StringBuilder();
		Boolean isExisting = false;
		
		BigInteger songTitleHash = hashMusicTitle(title);
		
		if(isSharedNode(ip)) {
			isExisting = findSongInST(ip, title, sb, isExisting, songTitleHash, ip);
			isExisting = deleteSongInST(ip, title, sb, isExisting, songTitleHash, ip);
		} else {
			BigInteger successor = findSuccessor(ip);
			
			isExisting = findSongInST(ip, title, sb, isExisting, songTitleHash, successor);
			isExisting = deleteSongInST(ip, title, sb, isExisting, songTitleHash, successor);
		}
		
		if(isExisting) {
			System.out.println("    return success");
		} else {
			System.out.println("    return failed");
		}
		
	}

	/************************************************
	 * 
	 * private methods
	 * 
	 **************************************************/
	
	/**
	 * check all parameters according to function name
	 */
	private static void checkParams(String tempString, String[] params, String name) {
		if(name.equals("addNode") || name.equals("removeNode")) {
			if(params.length != 1) {
				System.out.println("Error: there is bad format at \"" + tempString + "\"!");
				System.out.println("Sample: " + name + "(\"129.210.16.48\")");
				System.exit(0);
			}
		} else {
			if(params.length != 2) {
				System.out.println("Error: there is bad format at \"" + tempString + "\"!");
				System.out.println("Sample: " + name + "(\"129.210.16.48\", \"Listen to the Music\")");
				System.exit(0);
			}
		}
	}
	
	/**
	 * delete song from Song Table
	 */
	private static Boolean deleteSongInST(BigInteger ip, String title, StringBuilder sb, Boolean isExisting, BigInteger songTitleHash, BigInteger ipOfFT) {
		if(isSharedNode(songTitleHash)) {
			Node sharedNode = getSharedNode(songTitleHash);
			
			if(sharedNode != null) {
				for(SongTableItem stItem : sharedNode.getSongTable()) {
					if(stItem.getTitle().equals(title)) {
						if(stItem.getIP().compareTo(ip) == 0) {
							sharedNode.getSongTable().remove(stItem);
							isExisting = true;
							break;
						} else {
							isExisting = false;
							System.out.println("    cannot delete, only owner can delete!");
						}
					}
				}
			}
		} else {
			Node tempNode = getSharedNode(ipOfFT);
			
			if(tempNode != null) {
				FingerTableItem fti = findSuccessorInFingerTable(songTitleHash, tempNode, sb);
				BigInteger successor = fti.getSuccessor();
				
				Node sharedNode = getSharedNode(successor);
				
				for(SongTableItem stItem : sharedNode.getSongTable()) {
					if(stItem.getTitle().equals(title)) {
						if(stItem.getIP().compareTo(ip) == 0) {
							sharedNode.getSongTable().remove(stItem);
							isExisting = true;
							break;
						} else {
							isExisting = false;
							System.out.println("    cannot delete, only owner can delete!");
						}
					}
				}
			}
		}
		
		return isExisting;
	}

	/**
	 * find song from Song Table
	 */
	private static Boolean findSongInST(BigInteger ip, String title, StringBuilder sb, Boolean isExisting, BigInteger songTitleHash, BigInteger ipOfFT) {
		if(isSharedNode(songTitleHash)) {
			Node sharedNode = getSharedNode(songTitleHash);
			
			if(sharedNode != null) {
				for(SongTableItem stItem : sharedNode.getSongTable()) {
					if(stItem.getTitle().equals(title)) {
						isExisting = true;
						sb.append(" -> " + convertBigIntegerToIP(sharedNode.getIP()) + "(song at this node)");
						break;
					}
				}
			}
		} else {
			Node tempNode = getSharedNode(ipOfFT);
			
			if(tempNode != null) {
				FingerTableItem fti = findSuccessorInFingerTable(songTitleHash, tempNode, sb);
				BigInteger successor = fti.getSuccessor();
				
				Node sharedNode = getSharedNode(successor);
				
				for(SongTableItem stItem : sharedNode.getSongTable()) {
					if(stItem.getTitle().equals(title)) {
						isExisting = true;
						sb.append(" -> " + convertBigIntegerToIP(sharedNode.getIP()) + "(song at this node)");
						break;
					}
				}
			}
		}
		return isExisting;
	}
	
	/**
	 * store song into Song Table
	 */
	private static Boolean storeSongIntoST(BigInteger ip, String title, BigInteger songTitleHash, BigInteger ipOfFTNode) {
		Boolean result = false;
		if(isSharedNode(songTitleHash)) {
			Node sharedNode = getSharedNode(songTitleHash);
			
			if(sharedNode != null) {
				result = true;
				sharedNode.getSongTable().add(new SongTableItem(title, ip));
			}
		} else {
			Node tempNode = getSharedNode(ipOfFTNode);
			
			if(tempNode != null) {
				BigInteger successor = findSuccessorInFingerTable(songTitleHash, tempNode, null).getSuccessor();
				
				//System.out.println(successor);
				
				Node sharedNode = getSharedNode(successor);
				
				result = true;
				sharedNode.getSongTable().add(new SongTableItem(title, ip));
			}
		}
		
		return result;
	}

	/**
	 * find successor from Finger Table of specific sharing node
	 */
	private static FingerTableItem findSuccessorInFingerTable(BigInteger songTitleHash, Node sharedNode, StringBuilder sb) {
		FingerTableItem result = null;
		
		if(isInNodeAndSuccessor(songTitleHash, sharedNode)) {
			ArrayList<BigInteger> list = new ArrayList<BigInteger>();
			
			for(FingerTableItem fti : sharedNode.getFingerTable()) {
				list.add(fti.getJThSuccessor());
			}
			
			if(list.contains(songTitleHash)) {
				for(FingerTableItem fti : sharedNode.getFingerTable()) {
					if(fti.getJThSuccessor().compareTo(songTitleHash) == 0) {
						result = fti;
						break;
					}
				}
			} else {
				list.add(songTitleHash);
				Collections.sort(list);
				
				int index = list.indexOf(songTitleHash);
				BigInteger jThSuccessor = list.get((index+list.size()-1)%list.size());
				
				for(FingerTableItem fti : sharedNode.getFingerTable()) {
					if(fti.getJThSuccessor().compareTo(jThSuccessor) == 0) {
						result = fti;
						break;
					}
				}
			}
		} else {
			ArrayList<BigInteger> list = new ArrayList<BigInteger>();
			
			for(FingerTableItem fti : sharedNode.getFingerTable()) {
				if(!list.contains(fti.getSuccessor())) {
					list.add(fti.getSuccessor());
				}
			}
			
			list.add(songTitleHash);
			Collections.sort(list);
			
			int index = list.indexOf(songTitleHash);
			BigInteger successor = list.get((index+list.size()-1)%list.size());
			
			BigInteger jThSuccessor = new BigInteger("0");
			FingerTableItem tempFti = null;
			
			for(int i=0; i<sharedNode.getFingerTable().size(); i++) {
				FingerTableItem fti = sharedNode.getFingerTable().get(i);
				if(fti.getSuccessor().compareTo(successor) != 0) {
					continue;
				} else {
					if(fti.getJThSuccessor().compareTo(jThSuccessor) == 1) {
						tempFti = fti;
					}
				}
			}
			
			if(sb != null) {
				BigInteger temp = tempFti.getSuccessor();
				String successorIP = convertBigIntegerToIP(temp);
				sb.append(" -> " + successorIP + "(finger table entry j=" + tempFti.getIndex() + ")");
			}
			
			return findSuccessorInFingerTable(songTitleHash, getSharedNode(successor), sb);
		}
		
		return result;
	}

	/**
	 * judge whether a node falls between a sharing node and its successor
	 */
	private static boolean isInNodeAndSuccessor(BigInteger songTitleHash, Node sharedNode) {
		Boolean result = false;
		
		BigInteger source = sharedNode.getIP();
		BigInteger successor = findSuccessor(source);
		
		if(source.compareTo(successor) == 0) {
			result = true;
		} else {
			List<BigInteger> tempList = new ArrayList<BigInteger>();
			
			tempList.add(source);
			tempList.add(successor);
			tempList.add(songTitleHash);
			
			Collections.sort(tempList);
			
			int index = tempList.indexOf(source);
			int index2 = (index+2) % tempList.size();
			
			if(tempList.get(index2).compareTo(successor) == 0) {
				result = true;
			}
		}
		
		return result;
	}

	/**
	 * judge Whether a node is sharing node
	 */
	private static Boolean isSharedNode(BigInteger ip) {
		Boolean isFind = false;
		
		for(Node node : sharedNodes) {
			if(node.getIP().compareTo(ip) == 0) {
				isFind = true;
				break;
			}
		}
		
		return isFind;
	}
	
	/**
	 * get sharing node by IP address
	 */
	private static Node getSharedNode(BigInteger successor) {	
		for(Node node : sharedNodes) {
			if(node.getIP().compareTo(successor) == 0) {
				return node;
			}
		}
		
		return null;
	}
	
	/**
	 * find successor by its IP address
	 */
	private static BigInteger findSuccessor(BigInteger ip) {
		ArrayList<BigInteger> list = new ArrayList<BigInteger>();
		
		for(Node node : sharedNodes) {
			list.add(node.getIP());
		}
		
		if(!list.contains(ip)) list.add(ip);
		Collections.sort(list);
		
		int index = list.indexOf(ip);
		BigInteger result = list.get((index+1)%list.size());
		
		return result;
	}
	
	/**
	 * print information of all sharing nodes
	 */
//	private static void printShardNodesInfo() {
//		for(int i=0; i<sharedNodes.size(); i++) {
//			System.out.println("======Finger Table======");
//			
//			List<FingerTableItem> list = sharedNodes.get(i).getFingerTable();
//			Iterator<FingerTableItem> iter = list.iterator();
//			while (iter.hasNext()) {
//				FingerTableItem ftItem = (FingerTableItem)iter.next();
//				System.out.println(sharedNodes.get(i).getIP() + "," + ftItem.getIndex() + "," + ftItem.getJThSuccessor() + "," + ftItem.getSuccessor());
//			}
//			
//			List<SongTableItem> list2 = sharedNodes.get(i).getSongTable();
//			Iterator<SongTableItem> iter2 = list2.iterator();
//			if(iter2.hasNext()) System.out.println("======Song Table======");
//			
//			while (iter2.hasNext()) {
//				SongTableItem stItem = (SongTableItem)iter2.next();
//				System.out.println(sharedNodes.get(i).getIP() + "," + stItem.getTitle() + "," + stItem.getIP());
//			}
//		}
//	}
	
	/**
	 * convert BigInteger to IP
	 */
	private static String convertBigIntegerToIP(BigInteger number)
	{
	    String ip= "";

	    byte[] numbers = number.toByteArray();
	    
	    for(int k=0; k<4-numbers.length; k++) {
	    	ip += "0.";
	    }
	    
	    if(numbers.length == 5) {
		    for (int j = 1; j < numbers.length; j++) {
	    		int num = (numbers[j] & 0xFF);
	    		ip += num+".";
		    }
	    } else {
	    	for (int j = 0; j < numbers.length; j++) {
	    		int num = (numbers[j] & 0xFF);
	    		ip += num+".";
		    }
	    }
	    
	    ip = ip.substring(0, ip.length()-1);
	    return ip;
	}
	
	/**
	 * convert BigInteger to Hex
	 */
	private static String convertBigIntegerToHex(BigInteger number)
	{
	    return "0x" + number.toString(16);
	}
	
	/**
	 * convert IP to BigInteger
	 */
	private static BigInteger convertIPToBigInteger(String ip) {
		StringBuilder sb = new StringBuilder();
		String num[] = ip.split("\\.");
		
		if(num.length != 4) {
			System.out.println("Error: invalid IP, each IP should have 4 bytes!");
			System.exit(0);
		}
		
		for(int i=0; i< num.length; i++) {
			String seg = num[i].trim();
			for(int j=0; j<seg.length(); j++) {
				if(!Character.isDigit(seg.charAt(j))) {
					System.out.println("Error: invalid IP, each segment of IP should be digital and between 0 and 255!");
					System.exit(0);
				}
			}
			
			int ipSegment = Integer.valueOf(seg); 
			if(ipSegment < 0 || ipSegment > 255) {
				System.out.println("Error: invalid IP, each segment of IP should be digital and between 0 and 255!");
				System.exit(0);
			}
			
			String binaryString = Integer.toBinaryString(ipSegment);
			while(binaryString.length() < 8) {
				binaryString = "0" + binaryString;
			}
			
			sb.append(binaryString);
		}
		
		return new BigInteger(sb.toString(), 2);
	}
	
	/**
	 * update successor of Finger Table of all sharing nodes
	 */
	private static void updateSuccessor() {
		for(int i=0; i<sharedNodes.size(); i++) {
			Node node = sharedNodes.get(i);
			List<FingerTableItem> list = node.getFingerTable();
			Iterator<FingerTableItem> iter = list.iterator();
			while (iter.hasNext()) {
				FingerTableItem ftItem = (FingerTableItem)iter.next();
				ftItem.setSuccessor(buildSuccessor(ftItem.getJThSuccessor(), node.getIP()));
			}
		}
	
	}
	
	/**
	 * build successor of Finger Table of specific node
	 */
	private static BigInteger buildSuccessor(BigInteger jThSuccessor, BigInteger ip) {
		BigInteger result = null;
		ArrayList<BigInteger> list = new ArrayList<BigInteger>();
		
		if(sharedNodes.size() == 0) {
			result = ip;
		}
		else if(sharedNodes.size() == 1) {
			list.add(sharedNodes.get(0).getIP());
			list.add(jThSuccessor);
			if(!list.contains(ip)) list.add(ip);
			
			Collections.sort(list);
			int index = list.indexOf(jThSuccessor);
			result = list.get((index+1)%list.size());
		} else {
			for(int i=0; i<sharedNodes.size(); i++) {
				list.add(sharedNodes.get(i).getIP());
			}
			
			list.add(jThSuccessor);
			if(!list.contains(ip)) list.add(ip);
			
			Collections.sort(list);
			int index = list.indexOf(jThSuccessor);
			result = list.get((index+1)%list.size());
		}
		
		return result;
	}
	
	/**
	 * hash a title to an IP address
	 */
	private static BigInteger hashMusicTitle(String title) {
		int i=0;
		int nCount = 1;
		BigInteger storedNodeIP = null;
		
		while(i<title.length()) {
			String tempStr = title.substring(i, (i+4)<title.length()?i+4:title.length());
			byte[] bytes = null;
			try {
				bytes = tempStr.getBytes("US-ASCII");
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			StringBuilder sb = new StringBuilder();
			
			for(byte bt : bytes) {
				String binaryString = Integer.toBinaryString(bt);
				
				while(binaryString.length() < 8) {
					binaryString = "0" + binaryString;
				}
				
				sb.append(binaryString);
			}
			
			while(sb.toString().length() < 32) {
				sb.append("0");
			}
			
			if(nCount%2 != 0) {
				sb.reverse();
			}
				
			if(nCount==1) {
				storedNodeIP = new BigInteger(sb.toString(), 2);
			} else {
				storedNodeIP = storedNodeIP.xor(new BigInteger(sb.toString(), 2));
			}
			
			i+=4;
			nCount++;
		}
		return storedNodeIP;
	}
}
