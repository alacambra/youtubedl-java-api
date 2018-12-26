package tech.lacambra.downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class ExecuteShellCommand {

  public String executeCommand(String command, Consumer<InputStream> streamConsumer) {

    Process p;
    try {
      p = Runtime.getRuntime().exec(command);
      streamConsumer.accept(p.getInputStream());
      p.waitFor();
      return readStream(getCorrectStream(p));

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private InputStream getCorrectStream(Process process) {
    if (process.exitValue() == 0) {
      return process.getInputStream();
    } else {
      return process.getErrorStream();
    }
  }

  private String readStream(InputStream stream) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    StringBuilder output = new StringBuilder();

    String line;
    while ((line = reader.readLine()) != null) {
      output.append(line + "\n");
    }

    return output.toString();
  }
}
