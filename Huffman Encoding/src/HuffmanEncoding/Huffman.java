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
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Node {

    Node left = null;
    Node right = null;
    char ch;
    int weight;
    Node up = null;
}

public class Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        String directory = "/users/siddharthsharma/NetBeansProjects/Huffman Encoding/Test Files/";
        Scanner input = new Scanner(System.in);
        System.out.print("Do you want to compress or decompress?: ");
        String choice = input.nextLine();
        if (choice.equals("compress")) {
            System.out.print("What is the name of the file?: ");
            String filename = input.nextLine();
            System.out.print("What is the name of the output file?: ");
            String outputfile = input.nextLine();
            compressFile(filename, directory, outputfile);
        } else if (choice.equals("decompress")) {
            System.out.print("What is the name of the file?: ");
            String filename = input.nextLine();
            System.out.print("What is the name of the output file?: ");
            String outputfile = input.nextLine();
            decompressFile(filename, directory, outputfile);
        }

    }

    public static Map<Character, Integer> getCharFreq(String directory, String filename) {
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
        Map<Character, Integer> x = heap(str);
        return x;
    }

    public static Map<Character, Integer> heap(String s) {

        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (char c : s.toCharArray()) {
            if (map.get(c) == null) {
                map.put(c, 1);
            } else {
                map.replace(c, map.get(c) + 1);
            }
        }
        return map;
    }

    public static Node HuffmanTree(Map<Character, Integer> charFreq) {
        int size = charFreq.size();

        Node[] nodes = new Node[size];
        int j = 0;
        for (Map.Entry<Character, Integer> entry : charFreq.entrySet()) {
            nodes[j] = new Node();
            nodes[j].weight = entry.getValue();
            nodes[j].ch = entry.getKey();

            j++;
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
                if ((nodes[j + 1] == null)) {
                    continue;
                } else if (nodes[j] == null) {
                    Node temp = nodes[j];
                    nodes[j] = nodes[j + 1];
                    nodes[j + 1] = temp;
                    swaps++;
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
        nodes[1] = temp;
        nodes[0] = null;
        return nodes;
    }

    public static Map<Character, String> keyCodes(Node tree) {

        ArrayList<Node> visited = new ArrayList<Node>();

        Node curr = tree;
        String currCode = "";
        Map<Character, String> codes = new HashMap<Character, String>();

        while (visited.contains(curr.left) == false || visited.contains(curr.right) == false || curr.up != null) {

            if ((curr.left == null || visited.contains(curr.left)) && (curr.right == null || visited.contains(curr.right))) {

                if (curr.ch != '\u0000') {
                    codes.put(curr.ch, currCode);
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

        return codes;
    }

    public static String encode(String s, Map<Character, String> c) {
        String out = "";
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < s.length(); j++) {
            sb.append(getCode(s.charAt(j), c));
        }
        out = sb.toString();
        return out;
    }

    public static String getCode(char c, Map<Character, String> x) {
        return x.get(c);
    }

    public static void compressFile(String filename, String directory, String outputfile) {
        String str = "";
        StringBuilder sb2 = new StringBuilder();
        String absolutePath = directory + File.separator + filename;
        try ( BufferedReader fileReader = new BufferedReader(new FileReader(absolutePath), 16384)) {
            String line = fileReader.readLine();
            while (line != null) {
                sb2.append(line).append(System.getProperty("line.separator"));
                line = fileReader.readLine();
            }
            fileReader.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        str = sb2.toString();
        Map<Character, Integer> x = heap(str);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Map<Character, String> key = keyCodes(HuffmanTree(x));
        StringBuilder sb = new StringBuilder();

        try {
            FileWriter fileWriter = new FileWriter(directory + File.separator + outputfile);
            sb.append("{");
            for (Map.Entry<Character, Integer> entry : x.entrySet()) {
                char ch = entry.getKey();
                int val = entry.getValue();
                sb.append(ch);
                sb.append("-:-");
                sb.append(String.valueOf(val));
                sb.append(",-,");
            }
            sb.append("}");
            String charFreq = sb.toString();
            fileWriter.write(charFreq);
            fileWriter.close();
        } catch (IOException e) {

        }

        try ( OutputStream outputStream = new FileOutputStream(directory + File.separator + outputfile, true)) {
            BufferedOutputStream bos = new BufferedOutputStream(outputStream, 16384);
            bos.write(stringToBytes(encode(str, key)));
            bos.close();
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

    public static void decompressFile(String compFile, String directory, String outputfile) throws FileNotFoundException {
        String absolutePath = directory + File.separator + compFile;
        String str = "";
        File file = new File(absolutePath);
        int ind = 0;
        byte[] bytes = new byte[(int) file.length()];
        try ( InputStream inputStream = new FileInputStream(absolutePath)) {
            BufferedInputStream bis = new BufferedInputStream(inputStream, 16384);
            bytes = bis.readAllBytes();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

        String keys = "";
        if ((char) bytes[0] != '{') {
            System.out.println("Undecodable");
            System.exit(0);
        } else {
            StringBuilder sb2 = new StringBuilder();
            for (int i = 1; i < bytes.length; i++) {
                if ((char) bytes[i] == '}') {
                    ind = i + 1;
                    break;
                } else {
                    sb2.append((char) bytes[i]);
                }
            }
            keys = sb2.toString();
        }
        String[] ArrOfKeys = keys.split(",-,");
        Map<Character, Integer> charFreq = new HashMap<Character, Integer>();
        for (String a : ArrOfKeys) {
            String[] b = a.split("-:-");
            charFreq.put(b[0].toCharArray()[0], Integer.valueOf(b[1]));
        }
        Node tree = HuffmanTree(charFreq);
        String out = decode(Arrays.copyOfRange(bytes, ind, bytes.length - 1), tree);
        try {
            FileWriter fileWriter = new FileWriter(directory + File.separator + outputfile);
            fileWriter.write(out);
            fileWriter.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public static String decode(byte[] bytes, Node tree) {
        String str = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0'));
        }
        str = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        Node curr = tree;
        for (int j = 0; j < str.length(); j++) {
            if (str.charAt(j) == '0') {
                curr = curr.left;
            } else {
                if (str.charAt(j) == '1') {
                    curr = curr.right;
                }
            }
            if (curr.ch != '\u0000') {
                sb2.append(curr.ch);
                curr = tree;
            }
        }
        return sb2.toString();
    }
}
