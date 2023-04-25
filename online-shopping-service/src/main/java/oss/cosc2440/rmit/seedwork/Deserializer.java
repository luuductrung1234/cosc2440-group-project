package oss.cosc2440.rmit.seedwork;

import oss.cosc2440.rmit.domain.Domain;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Deserializer {
  /**
   * Read a list of domain objects (of type TDomain) from data file
   *
   * @param clazz Class object of type TDomain
   * @return a list of domain objects (of type TDomain)
   * @throws IOException data file could not be read
   */
  public static <TDomain extends Domain<?>> List<TDomain> read(String pathToDataFile, Class<TDomain> clazz) throws IOException {
    Path path = Paths.get(pathToDataFile);
    if (!Files.isReadable(path))
      return new ArrayList<>();
    List<String> lines = Files.readAllLines(path);
    return lines.stream()
        .filter(line -> !Helpers.isNullOrEmpty(line))
        .map(line -> lineDeserialize(line, clazz))
        .filter(Objects::nonNull).collect(Collectors.toList());
  }

  /**
   * Deserialize given line of text into an instance of clazz
   *
   * @param line      text of serialized data
   * @param clazz     type of instance after deserialized
   * @param <TDomain> type of instance after deserialized
   * @return an instance of clazz
   */
  @SuppressWarnings("unchecked")
  private static <TDomain extends Domain<?>> TDomain lineDeserialize(String line, Class<TDomain> clazz) {
    try {
      Method deserialize = clazz.getMethod("deserialize", String.class);
      return (TDomain) deserialize.invoke(null, line);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      Logger.printError("Deserializer", "lineDeserialize", e);
      return null;
    }
  }
}
