package de.vonrauchhaupt.picutils;

import de.vonrauchhaupt.picutils.model.ScannerContextDto;
import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash;

import java.nio.file.Path;

public class Scanner {

    private static ScannerContextDto scannerContextDto;

    static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Scanner <root path of images> <--clean>");
            System.exit(1);
        }
        boolean cleanScan = false;
        if ( args.length == 2 && "--clean".equals(args[1]))
            cleanScan = true;
        scannerContextDto = new ScannerContextDto(Path.of(args[0]),cleanScan);
        scannerContextDto.scanRootDirectory();
        System.exit(0);
    }
}
