package de.vonrauchhaupt.picutils;

import de.vonrauchhaupt.picutils.model.ImageInformationDto;
import de.vonrauchhaupt.picutils.model.ScannerContextDto;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Set;

public class WrongDateImages {
    private static ScannerContextDto scannerContextDto;

    static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("DuplicateImages <root path of images>");
            System.exit(1);
        }
        scannerContextDto = new ScannerContextDto(Path.of(args[0]), false);
        scannerContextDto.loadFromFile();

        Set<ImageInformationDto> imageInformationSet = scannerContextDto.getImageInformationSet();
        imageInformationSet.removeIf(x -> !x.exists());

        for (ImageInformationDto imageInformationDto : imageInformationSet) {
            LocalDateTime dateFromExif = imageInformationDto.getDateFromExif();
            if (dateFromExif == null) {
                System.err.println("Could not read EXIF date of " + imageInformationDto.getPath().toAbsolutePath());
                continue;
            }
            LocalDateTime minDateFromPath = imageInformationDto.getMinDateFromPath();
            if (minDateFromPath == null) {
                System.err.println("Could not read date of directory " + imageInformationDto.getPath().toAbsolutePath());
                continue;
            }

            LocalDateTime maxDateFromPath = imageInformationDto.getMinDateFromPath();
            if (maxDateFromPath == null) {
                System.err.println("Could not read date of directory " + imageInformationDto.getPath().toAbsolutePath());
                continue;
            }

            if (dateFromExif.isBefore(minDateFromPath) || dateFromExif.isAfter(maxDateFromPath)) {
                System.err.println("!!!" + imageInformationDto.getPath().toAbsolutePath() + " has wrong EXIF date " +
                        dateFromExif.format(ImageInformationDto.FORMATTER) + " != " +
                        minDateFromPath.format(ImageInformationDto.FORMATTER) + "/" +
                        maxDateFromPath.format(ImageInformationDto.FORMATTER));
            }

        }


    }
}
