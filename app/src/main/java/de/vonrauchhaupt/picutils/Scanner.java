package de.vonrauchhaupt.picutils;

import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash;

import java.nio.file.Path;

public class Scanner {

    private static ScannerContextDto scannerContextDto;
    protected static PerceptiveHash pHash = new PerceptiveHash(32);

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Scanner <root path of images>");
            System.exit(1);
        }
        scannerContextDto = new ScannerContextDto(Path.of(args[0]));
        scannerContextDto.scanRootDirectory();
        System.exit(0);
    }
}
