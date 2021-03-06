package qowyn.ark.arrays;

import java.util.ArrayList;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonString;

import qowyn.ark.ArkArchive;

public class ArkArrayString extends ArrayList<String> implements ArkArray<String> {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public ArkArrayString() {}

  public ArkArrayString(ArkArchive archive, int dataSize) {
    int size = archive.getInt();

    for (int n = 0; n < size; n++) {
      add(archive.getString());
    }
  }

  public ArkArrayString(JsonArray a, int dataSize) {
    a.getValuesAs(JsonString.class).forEach(s -> this.add(s.getString()));
  }

  @Override
  public void collectNames(Set<String> nameTable) {}

  @Override
  public Class<String> getValueClass() {
    return String.class;
  }

  @Override
  public int calculateSize(boolean nameTable) {
    int size = Integer.BYTES;

    size += stream().mapToInt(ArkArchive::getStringLength).sum();

    return size;
  }

  @Override
  public JsonArray toJson() {
    JsonArrayBuilder jab = Json.createArrayBuilder();

    this.forEach(jab::add);

    return jab.build();
  }

  @Override
  public void write(ArkArchive archive) {
    archive.putInt(size());

    this.forEach(archive::putString);
  }

}
