/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HuffmanEncoding;

import static HuffmanEncoding.Huffman.compressFile;

/**
 *
 * @author siddharthsharma
 */
public class Encoder {

    public static void main(String args) {
        String directory = "/users/siddharthsharma/NetBeansProjects/Huffman Encoding/src/HuffmanEncoding";
        String outputfile = "output.txt";
        compressFile("test.txt", directory, outputfile);
    }

}
