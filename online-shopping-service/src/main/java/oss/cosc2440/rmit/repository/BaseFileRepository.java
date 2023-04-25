package oss.cosc2440.rmit.repository;


import oss.cosc2440.rmit.domain.Domain;
import oss.cosc2440.rmit.seedwork.Helpers;
import oss.cosc2440.rmit.seedwork.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class BaseFileRepository {
  private final String pathToDataFile;

  public BaseFileRepository(String pathToDataFile) {
    this.pathToDataFile = pathToDataFile;
  }

  /**
   * Read a list of domain objects (of type TDomain) from data file
   *
   * @param clazz Class object of type TDomain
   * @return a list of domain objects (of type TDomain)
   * @throws IOException data file could not be read
   */
  public <TDomain extends Domain<?>> List<TDomain> read(Class<TDomain> clazz) throws IOException {
    Path path = Paths.get(this.pathToDataFile);
    if (!Files.isReadable(path))
      return new ArrayList<>();
    List<String> lines = Files.readAllLines(path);
    return lines.stream()
        .filter(line -> !Helpers.isNullOrEmpty(line))
        .map(line -> lineDeserialize(line, clazz))
        .filter(Objects::nonNull).collect(Collectors.toList());
  }

  /**
   * Write a list of domain objects (of type TDomain) to data file
   *
   * @param records a list of domain objects (of type TDomain)
   * @throws IOException data file could not be written
   */
  public <TDomain extends Domain<?>> void write(List<TDomain> records) throws IOException {
    List<String> lines = records.stream()
        .map(Domain::serialize)
        .collect(Collectors.toList());
    String data = String.join("\n", lines);
    Path path = Paths.get(this.pathToDataFile);
    if (Helpers.isNullOrEmpty(data) && Files.exists(path)) {
      Files.delete(path);
      return;
    }
    Files.write(path, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
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
  private <TDomain extends Domain<?>> TDomain lineDeserialize(String line, Class<TDomain> clazz) {
    try {
      Method deserialize = clazz.getMethod("deserialize", String.class);
      return (TDomain) deserialize.invoke(null, line);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      Logger.printError(this.getClass().getName(), "lineDeserialize", e);
      return null;
    }
  }
}
