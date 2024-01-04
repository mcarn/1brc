/*
 *  Copyright 2023 The original authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package dev.morling.onebrc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CalculateAverage_mcarn {

    private static final String FILE = "./measurements.txt";

  private record ResultRow(double min, double mean, double max) {
    public String toString() {
      return round(min) + "/" + round(mean) + "/" + round(max);
    }

    private double round(double value) {
      return Math.round(value * 10.0) / 10.0;
    }
  }

    public static void main(String[] args) throws IOException {

        try (Stream<String> stream = Files.lines(Paths.get(FILE))) {
            Map<String, Set<Double>> lines = stream
                    .collect(Collectors.toMap(
                            line -> line.split(";")[0],
                            line -> {
                                String[] parts = line.split(";");
                                return Set.of(Double.parseDouble(parts[1]));
                            },
                            (set1, set2) -> Stream.concat(set1.stream(), set2.stream())
                                    .collect(Collectors.toSet())));

            Map<String, ResultRow> measurements = lines.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> aaa(entry.getValue())));

            System.out.println(measurements);
        }

    }

    private static ResultRow aaa(Set<Double> set) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        double avg = 0.0;

        for (double v : set) {
            if (v > max) {
                max = v;
            }
            if (v < min) {
                min = v;
            }
            avg += v;
        }

        return new ResultRow(min, avg / set.size(), max);
    }

}
