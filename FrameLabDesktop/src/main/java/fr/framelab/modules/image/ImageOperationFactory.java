package fr.framelab.modules.image;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageOperationFactory {
    @FunctionalInterface
    interface OperationCreator {
        ImageOperation create(Integer param);
    }

    private static final Map<String, OperationCreator> REGISTRY = Map.of(
            "BlacknWhiteOperation", p -> new BlacknWhiteOperation(),
            "InvertOperation", p -> new InvertOperation(),
            "BrightnessOperation", p -> new BrightnessOperation(p != null ? p : 0),
            "ContrastOperation", p -> new ContrastOperation(p != null ? p : 0),
            "RotationOperation", p -> new RotationOperation(p != null ? p : 0)
    );

    public static ImageOperation create(String type, Integer param) {
        OperationCreator creator = REGISTRY.get(type);
        if (creator == null) throw new IllegalArgumentException("Type inconnu: " + type);
        return creator.create(param);
    }

    public static List<String> getAvailableTypes() {
        return new ArrayList<>(REGISTRY.keySet());
    }
}