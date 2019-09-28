/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HuffmanEncoding;

/**
 *
 * @author siddharthsharma
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.lang.Math;

class Node {

    Node left = null;
    Node right = null;
    char ch;
    int weight;
    Node up = null;
}

class codes {

    char ch;
    String code;
}

class charFrequency {

    char ch;
    int freq;
}

public class Huffman {

    public static void main(String[] args) {

    }

    public static ArrayList<charFrequency> heap(String s) {
        ArrayList<charFrequency> charFreq = new ArrayList<charFrequency>();
        for (char c : s.toCharArray()) {
            boolean x = true;
            for (int i = 0; i < charFreq.size(); i++) {
                if (charFreq.get(i).ch == c) {
                    charFreq.get(i).freq++;
                    x = false;
                }
            }
            if (x == true) {
                charFrequency temp = new charFrequency();
                temp.ch = c;
                temp.freq = 1;
                charFreq.add(temp);
            }
        }
        return charFreq;
    }

    public static Node HuffmanTree(ArrayList<charFrequency> charFreq) {
        int size = charFreq.size();
        Node[] nodes = new Node[size];
        for (int i = 0; i < size; i++) {
            nodes[i] = new Node();
            nodes[i].weight = charFreq.get(i).freq;
            nodes[i].ch = charFreq.get(i).ch;
        }
        Node out = new Node();
        for (int i = 0; i < size - 1; i++) {
            nodes = buildTree(nodes);
        }
        nodes = bubbleSort(nodes);
        out = nodes[0];
        return out;
    }

    public static Node[] bubbleSort(Node[] nodes) {
        for (int i = 0; i < nodes.length - 1; i++) {
            int swaps = 0;
            for (int j = 0; j < nodes.length - i - 1; j++) {
                if (nodes[j] == null) {
                    Node temp = nodes[j];
                    nodes[j] = nodes[j + 1];
                    nodes[j + 1] = temp;
                    swaps++;

                } else if (nodes[j + 1] == null) {
                    continue;
                } else if (nodes[j].weight > nodes[j + 1].weight) {
                    Node temp = nodes[j];
                    nodes[j] = nodes[j + 1];
                    nodes[j + 1] = temp;
                    swaps++;
                }
            }
            if (swaps == 0) {
                break;
            }
        }
        return nodes;
    }

    public static Node[] buildTree(Node[] nodes) {
        nodes = bubbleSort(nodes);
        Node temp = new Node();
        temp.left = nodes[0];
        temp.right = nodes[1];
        temp.weight = nodes[0].weight + nodes[1].weight;
        temp.left.up = temp;
        temp.right.up = temp;
        nodes[0] = null;
        nodes[1] = temp;
        return nodes;
    }

    public static ArrayList<codes> keyCodes(Node tree) {
        ArrayList<Node> visited = new ArrayList<Node>();
        ArrayList<codes> out = new ArrayList<codes>();
        Node curr = tree;
        String currCode = "";
        while (visited.contains(curr.left) == false || visited.contains(curr.right) == false || curr.up != null) {
            if ((curr.left == null || visited.contains(curr.left)) && (curr.right == null || visited.contains(curr.right))) {
                codes x = new codes();
                x.ch = curr.ch;
                x.code = currCode;
                if (x.ch != '\u0000') {
                    out.add(x);
                }
                visited.add(curr);
                curr = curr.up;
                currCode = currCode.substring(0, currCode.length() - 1);
            } else if (visited.contains(curr.left) == false && curr.left != null) {
                curr = curr.left;
                currCode = currCode + "0";
            } else if (visited.contains(curr.right) == false && curr.right != null) {
                curr = curr.right;
                currCode = currCode + "1";
            }
        }
        return out;
    }

    public static String encode(String s, ArrayList<codes> c) {
        String out = "";
        for (int j = 0; j < s.length(); j++) {
            out = out + getCode(s.charAt(j), c);
        }
        return out;
    }

    public static String getCode(char c, ArrayList<codes> x) {
        for (int i = 0; i < x.size(); i++) {
            if (x.get(i).ch == c) {
                return x.get(i).code;
            }
        }
        return "";
    }

    public static void compressFile(String filename, String directory, String outputfile) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        String absolutePath = directory + File.separator + filename;
        String str = "";
        try ( FileReader fileReader = new FileReader(absolutePath)) {
            int ch = fileReader.read();
            while (ch != -1) {
                str = str + (char) ch;
                ch = fileReader.read();
            }

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        ArrayList<charFrequency> x = heap(str);
        ArrayList<codes> key = keyCodes(HuffmanTree(x));
        try ( OutputStream outputStream = new FileOutputStream(outputfile)) {
            outputStream.write(stringToBytes(encode(str, key)));
        } catch (IOException e) {

        }
    }

    public static byte[] stringToBytes(String s) {
        int byteNum = (int) Math.ceil(s.length() / 8);
        byte[] bytes = new byte[byteNum];
        for (int i = 0; i < byteNum; i++) {
            String subByte;
            if (i * 8 <= (s.length() - 8)) {
                subByte = s.substring(i * 8, i * 8 + 8);
            } else {
                subByte = s.substring(i * 8);
                subByte += "00000000".substring(0, 8 - subByte.length());
            }
            bytes[i] = (byte) Integer.parseInt(subByte, 2);
        }
        return bytes;
    }
}
