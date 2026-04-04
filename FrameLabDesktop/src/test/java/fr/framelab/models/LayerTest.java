package fr.framelab.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LayerTest {
    private Layer layer;
    private final int PROJECT_ID = 1;
    private final int INDEX = 0;
    private final int LAYER_TYPE = 0;

    @BeforeEach
    void setUp() {
        layer = new Layer(PROJECT_ID, INDEX, LAYER_TYPE);
    }

    @Test
    void shouldNotAllowIdModification() {
        // ARRANGE - Préparer les données
        layer.setId(1);
        int originalId = layer.getId();

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            layer.setId(99);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(UnsupportedOperationException.class, thrownException);
        assertEquals("ID cannot be modified once set", thrownException.getMessage());
        assertEquals(originalId, layer.getId());
    }

    @Test
    void shouldRejectNegativeProjectId() {
        // ARRANGE - Préparer les données
        int projectId = -1;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            new Layer(projectId, INDEX, LAYER_TYPE);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("The project ID cannot be less than 0", thrownException.getMessage());
    }

    @Test
    void shouldAcceptValidProjectId() {
        // ARRANGE - Préparer les données
        int projectId = 10;

        // ACT - Exécuter l'action à tester
        Layer newLayer = new Layer(projectId, INDEX, LAYER_TYPE);

        // ASSERT - Vérifier le résultat
        assertEquals(projectId, newLayer.getProjectId());
    }

    @Test
    void shouldAcceptZeroProjectId() {
        // ARRANGE - Préparer les données
        int projectId = 0;

        // ACT - Exécuter l'action à tester
        Layer newLayer = new Layer(projectId, INDEX, LAYER_TYPE);

        // ASSERT - Vérifier le résultat
        assertEquals(projectId, newLayer.getProjectId());
    }

    @Test
    void shouldRejectNegativeIndex() {
        // ARRANGE - Préparer les données
        int index = -1;

        // ACT - Exécuter l'action à tester
        Exception thrownException = null;
        try {
            new Layer(PROJECT_ID, index, LAYER_TYPE);
        } catch (Exception e) {
            thrownException = e;
        }

        // ASSERT - Vérifier le résultat
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
        assertEquals("The index cannot be less than 0", thrownException.getMessage());
    }

    @Test
    void shouldAcceptValidIndex() {
        // ARRANGE - Préparer les données
        int index = 5;

        // ACT - Exécuter l'action à tester
        Layer newLayer = new Layer(PROJECT_ID, index, LAYER_TYPE);

        // ASSERT - Vérifier le résultat
        assertEquals(index, newLayer.getIndex());
    }

    @Test
    void shouldAcceptZeroIndex() {
        // ARRANGE - Préparer les données
        int index = 0;

        // ACT - Exécuter l'action à tester
        Layer newLayer = new Layer(PROJECT_ID, index, LAYER_TYPE);

        // ASSERT - Vérifier le résultat
        assertEquals(index, newLayer.getIndex());
    }
}