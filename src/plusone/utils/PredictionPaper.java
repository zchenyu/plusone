package plusone.utils;

import java.util.Set;

public interface PredictionPaper {
    public Integer getTrainingTf(Integer word);
    public Set<Integer> getTrainingWords();
}