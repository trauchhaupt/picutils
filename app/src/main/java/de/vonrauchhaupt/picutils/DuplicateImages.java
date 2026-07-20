package de.vonrauchhaupt.picutils;

import de.vonrauchhaupt.picutils.model.ImageInformationDto;
import de.vonrauchhaupt.picutils.model.ScannerContextDto;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DuplicateImages {
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

        Map<Integer, List<ImageInformationDto>> groupedByHash = imageInformationSet.stream()
                .collect(Collectors.groupingBy(ImageInformationDto::hashCode));

        for (Map.Entry<Integer, List<ImageInformationDto>> integerListEntry : groupedByHash.entrySet()) {
            if (integerListEntry.getValue().size() <= 2)
                continue;
            System.out.println("Potential Duplicate " + integerListEntry.getKey());
            for (ImageInformationDto imageInformationDto : integerListEntry.getValue().stream()
                    .sorted(Comparator.comparing(ImageInformationDto::getWidth)
                            .thenComparing(ImageInformationDto::getHeight)
                            .thenComparing(x -> x.getPath().toAbsolutePath().toString()))
                    .toList()) {
                System.out.println("del " + imageInformationDto.getPath().toAbsolutePath() + " (" + imageInformationDto.getWidth() + "x" + imageInformationDto.getHeight() + ")");
            }
        }

    }
}
