package qowyn.ark.properties;

import java.util.Set;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import qowyn.ark.ArkArchive;
import qowyn.ark.structs.Struct;
import qowyn.ark.structs.StructRegistry;
import qowyn.ark.types.ArkName;

public class PropertyStruct extends PropertyBase<Struct> {

  public PropertyStruct(String name, String typeName, Struct value) {
    super(name, typeName, 0, value);
  }

  public PropertyStruct(String name, String typeName, int index, Struct value) {
    super(name, typeName, index, value);
  }

  public PropertyStruct(ArkArchive archive, PropertyArgs args) {
    super(archive, args);
    ArkName structType = archive.getName();

    int position = archive.position();
    try {
      value = StructRegistry.read(archive, structType);
    } catch (UnreadablePropertyException upe) {
      // skip struct
      archive.position(position + dataSize);
    }
  }

  public PropertyStruct(JsonObject o) {
    super(o);
    ArkName structType = new ArkName(o.getString("structType"));
    value = StructRegistry.read(o.get("value"), structType);
  }

  @Override
  public Class<Struct> getValueClass() {
    return Struct.class;
  }

  @Override
  public Struct getValue() {
    return value;
  }

  @Override
  public void setValue(Struct value) {
    this.value = value;
  }

  @Override
  protected void serializeValue(JsonObjectBuilder job) {
    job.add("structType", value.getStructType().toString());
    job.add("value", value.toJson());
  }

  @Override
  protected void writeValue(ArkArchive archive) {
    archive.putName(value.getStructType());
    value.write(archive);
  }

  @Override
  protected int calculateAdditionalSize(boolean nameTable) {
    return ArkArchive.getNameLength(value.getStructType(), nameTable);
  }

  @Override
  public int calculateDataSize(boolean nameTable) {
    return value.getSize(nameTable);
  }

  @Override
  public void collectNames(Set<String> nameTable) {
    super.collectNames(nameTable);
    value.collectNames(nameTable);
  }

}
