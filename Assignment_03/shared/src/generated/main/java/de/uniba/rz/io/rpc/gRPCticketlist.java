// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ticketManagement.proto

package de.uniba.rz.io.rpc;

/**
 * Protobuf type {@code gRPCticketlist}
 */
public  final class gRPCticketlist extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:gRPCticketlist)
    gRPCticketlistOrBuilder {
private static final long serialVersionUID = 0L;
  // Use gRPCticketlist.newBuilder() to construct.
  private gRPCticketlist(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private gRPCticketlist() {
    gRPCticketlisElement_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private gRPCticketlist(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              gRPCticketlisElement_ = new java.util.ArrayList<de.uniba.rz.io.rpc.gRPCticket>();
              mutable_bitField0_ |= 0x00000001;
            }
            gRPCticketlisElement_.add(
                input.readMessage(de.uniba.rz.io.rpc.gRPCticket.parser(), extensionRegistry));
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000001) != 0)) {
        gRPCticketlisElement_ = java.util.Collections.unmodifiableList(gRPCticketlisElement_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return de.uniba.rz.io.rpc.TicketManagement.internal_static_gRPCticketlist_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return de.uniba.rz.io.rpc.TicketManagement.internal_static_gRPCticketlist_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            de.uniba.rz.io.rpc.gRPCticketlist.class, de.uniba.rz.io.rpc.gRPCticketlist.Builder.class);
  }

  public static final int GRPCTICKETLISELEMENT_FIELD_NUMBER = 1;
  private java.util.List<de.uniba.rz.io.rpc.gRPCticket> gRPCticketlisElement_;
  /**
   * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
   */
  public java.util.List<de.uniba.rz.io.rpc.gRPCticket> getGRPCticketlisElementList() {
    return gRPCticketlisElement_;
  }
  /**
   * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
   */
  public java.util.List<? extends de.uniba.rz.io.rpc.gRPCticketOrBuilder> 
      getGRPCticketlisElementOrBuilderList() {
    return gRPCticketlisElement_;
  }
  /**
   * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
   */
  public int getGRPCticketlisElementCount() {
    return gRPCticketlisElement_.size();
  }
  /**
   * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
   */
  public de.uniba.rz.io.rpc.gRPCticket getGRPCticketlisElement(int index) {
    return gRPCticketlisElement_.get(index);
  }
  /**
   * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
   */
  public de.uniba.rz.io.rpc.gRPCticketOrBuilder getGRPCticketlisElementOrBuilder(
      int index) {
    return gRPCticketlisElement_.get(index);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    for (int i = 0; i < gRPCticketlisElement_.size(); i++) {
      output.writeMessage(1, gRPCticketlisElement_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < gRPCticketlisElement_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, gRPCticketlisElement_.get(i));
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof de.uniba.rz.io.rpc.gRPCticketlist)) {
      return super.equals(obj);
    }
    de.uniba.rz.io.rpc.gRPCticketlist other = (de.uniba.rz.io.rpc.gRPCticketlist) obj;

    if (!getGRPCticketlisElementList()
        .equals(other.getGRPCticketlisElementList())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getGRPCticketlisElementCount() > 0) {
      hash = (37 * hash) + GRPCTICKETLISELEMENT_FIELD_NUMBER;
      hash = (53 * hash) + getGRPCticketlisElementList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static de.uniba.rz.io.rpc.gRPCticketlist parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static de.uniba.rz.io.rpc.gRPCticketlist parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static de.uniba.rz.io.rpc.gRPCticketlist parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static de.uniba.rz.io.rpc.gRPCticketlist parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static de.uniba.rz.io.rpc.gRPCticketlist parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static de.uniba.rz.io.rpc.gRPCticketlist parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static de.uniba.rz.io.rpc.gRPCticketlist parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static de.uniba.rz.io.rpc.gRPCticketlist parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static de.uniba.rz.io.rpc.gRPCticketlist parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static de.uniba.rz.io.rpc.gRPCticketlist parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static de.uniba.rz.io.rpc.gRPCticketlist parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static de.uniba.rz.io.rpc.gRPCticketlist parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(de.uniba.rz.io.rpc.gRPCticketlist prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code gRPCticketlist}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:gRPCticketlist)
      de.uniba.rz.io.rpc.gRPCticketlistOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return de.uniba.rz.io.rpc.TicketManagement.internal_static_gRPCticketlist_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return de.uniba.rz.io.rpc.TicketManagement.internal_static_gRPCticketlist_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              de.uniba.rz.io.rpc.gRPCticketlist.class, de.uniba.rz.io.rpc.gRPCticketlist.Builder.class);
    }

    // Construct using de.uniba.rz.io.rpc.gRPCticketlist.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getGRPCticketlisElementFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (gRPCticketlisElementBuilder_ == null) {
        gRPCticketlisElement_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        gRPCticketlisElementBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return de.uniba.rz.io.rpc.TicketManagement.internal_static_gRPCticketlist_descriptor;
    }

    @java.lang.Override
    public de.uniba.rz.io.rpc.gRPCticketlist getDefaultInstanceForType() {
      return de.uniba.rz.io.rpc.gRPCticketlist.getDefaultInstance();
    }

    @java.lang.Override
    public de.uniba.rz.io.rpc.gRPCticketlist build() {
      de.uniba.rz.io.rpc.gRPCticketlist result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public de.uniba.rz.io.rpc.gRPCticketlist buildPartial() {
      de.uniba.rz.io.rpc.gRPCticketlist result = new de.uniba.rz.io.rpc.gRPCticketlist(this);
      int from_bitField0_ = bitField0_;
      if (gRPCticketlisElementBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          gRPCticketlisElement_ = java.util.Collections.unmodifiableList(gRPCticketlisElement_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.gRPCticketlisElement_ = gRPCticketlisElement_;
      } else {
        result.gRPCticketlisElement_ = gRPCticketlisElementBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof de.uniba.rz.io.rpc.gRPCticketlist) {
        return mergeFrom((de.uniba.rz.io.rpc.gRPCticketlist)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(de.uniba.rz.io.rpc.gRPCticketlist other) {
      if (other == de.uniba.rz.io.rpc.gRPCticketlist.getDefaultInstance()) return this;
      if (gRPCticketlisElementBuilder_ == null) {
        if (!other.gRPCticketlisElement_.isEmpty()) {
          if (gRPCticketlisElement_.isEmpty()) {
            gRPCticketlisElement_ = other.gRPCticketlisElement_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureGRPCticketlisElementIsMutable();
            gRPCticketlisElement_.addAll(other.gRPCticketlisElement_);
          }
          onChanged();
        }
      } else {
        if (!other.gRPCticketlisElement_.isEmpty()) {
          if (gRPCticketlisElementBuilder_.isEmpty()) {
            gRPCticketlisElementBuilder_.dispose();
            gRPCticketlisElementBuilder_ = null;
            gRPCticketlisElement_ = other.gRPCticketlisElement_;
            bitField0_ = (bitField0_ & ~0x00000001);
            gRPCticketlisElementBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getGRPCticketlisElementFieldBuilder() : null;
          } else {
            gRPCticketlisElementBuilder_.addAllMessages(other.gRPCticketlisElement_);
          }
        }
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      de.uniba.rz.io.rpc.gRPCticketlist parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (de.uniba.rz.io.rpc.gRPCticketlist) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<de.uniba.rz.io.rpc.gRPCticket> gRPCticketlisElement_ =
      java.util.Collections.emptyList();
    private void ensureGRPCticketlisElementIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        gRPCticketlisElement_ = new java.util.ArrayList<de.uniba.rz.io.rpc.gRPCticket>(gRPCticketlisElement_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        de.uniba.rz.io.rpc.gRPCticket, de.uniba.rz.io.rpc.gRPCticket.Builder, de.uniba.rz.io.rpc.gRPCticketOrBuilder> gRPCticketlisElementBuilder_;

    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public java.util.List<de.uniba.rz.io.rpc.gRPCticket> getGRPCticketlisElementList() {
      if (gRPCticketlisElementBuilder_ == null) {
        return java.util.Collections.unmodifiableList(gRPCticketlisElement_);
      } else {
        return gRPCticketlisElementBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public int getGRPCticketlisElementCount() {
      if (gRPCticketlisElementBuilder_ == null) {
        return gRPCticketlisElement_.size();
      } else {
        return gRPCticketlisElementBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public de.uniba.rz.io.rpc.gRPCticket getGRPCticketlisElement(int index) {
      if (gRPCticketlisElementBuilder_ == null) {
        return gRPCticketlisElement_.get(index);
      } else {
        return gRPCticketlisElementBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public Builder setGRPCticketlisElement(
        int index, de.uniba.rz.io.rpc.gRPCticket value) {
      if (gRPCticketlisElementBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureGRPCticketlisElementIsMutable();
        gRPCticketlisElement_.set(index, value);
        onChanged();
      } else {
        gRPCticketlisElementBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public Builder setGRPCticketlisElement(
        int index, de.uniba.rz.io.rpc.gRPCticket.Builder builderForValue) {
      if (gRPCticketlisElementBuilder_ == null) {
        ensureGRPCticketlisElementIsMutable();
        gRPCticketlisElement_.set(index, builderForValue.build());
        onChanged();
      } else {
        gRPCticketlisElementBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public Builder addGRPCticketlisElement(de.uniba.rz.io.rpc.gRPCticket value) {
      if (gRPCticketlisElementBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureGRPCticketlisElementIsMutable();
        gRPCticketlisElement_.add(value);
        onChanged();
      } else {
        gRPCticketlisElementBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public Builder addGRPCticketlisElement(
        int index, de.uniba.rz.io.rpc.gRPCticket value) {
      if (gRPCticketlisElementBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureGRPCticketlisElementIsMutable();
        gRPCticketlisElement_.add(index, value);
        onChanged();
      } else {
        gRPCticketlisElementBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public Builder addGRPCticketlisElement(
        de.uniba.rz.io.rpc.gRPCticket.Builder builderForValue) {
      if (gRPCticketlisElementBuilder_ == null) {
        ensureGRPCticketlisElementIsMutable();
        gRPCticketlisElement_.add(builderForValue.build());
        onChanged();
      } else {
        gRPCticketlisElementBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public Builder addGRPCticketlisElement(
        int index, de.uniba.rz.io.rpc.gRPCticket.Builder builderForValue) {
      if (gRPCticketlisElementBuilder_ == null) {
        ensureGRPCticketlisElementIsMutable();
        gRPCticketlisElement_.add(index, builderForValue.build());
        onChanged();
      } else {
        gRPCticketlisElementBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public Builder addAllGRPCticketlisElement(
        java.lang.Iterable<? extends de.uniba.rz.io.rpc.gRPCticket> values) {
      if (gRPCticketlisElementBuilder_ == null) {
        ensureGRPCticketlisElementIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, gRPCticketlisElement_);
        onChanged();
      } else {
        gRPCticketlisElementBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public Builder clearGRPCticketlisElement() {
      if (gRPCticketlisElementBuilder_ == null) {
        gRPCticketlisElement_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        gRPCticketlisElementBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public Builder removeGRPCticketlisElement(int index) {
      if (gRPCticketlisElementBuilder_ == null) {
        ensureGRPCticketlisElementIsMutable();
        gRPCticketlisElement_.remove(index);
        onChanged();
      } else {
        gRPCticketlisElementBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public de.uniba.rz.io.rpc.gRPCticket.Builder getGRPCticketlisElementBuilder(
        int index) {
      return getGRPCticketlisElementFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public de.uniba.rz.io.rpc.gRPCticketOrBuilder getGRPCticketlisElementOrBuilder(
        int index) {
      if (gRPCticketlisElementBuilder_ == null) {
        return gRPCticketlisElement_.get(index);  } else {
        return gRPCticketlisElementBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public java.util.List<? extends de.uniba.rz.io.rpc.gRPCticketOrBuilder> 
         getGRPCticketlisElementOrBuilderList() {
      if (gRPCticketlisElementBuilder_ != null) {
        return gRPCticketlisElementBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(gRPCticketlisElement_);
      }
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public de.uniba.rz.io.rpc.gRPCticket.Builder addGRPCticketlisElementBuilder() {
      return getGRPCticketlisElementFieldBuilder().addBuilder(
          de.uniba.rz.io.rpc.gRPCticket.getDefaultInstance());
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public de.uniba.rz.io.rpc.gRPCticket.Builder addGRPCticketlisElementBuilder(
        int index) {
      return getGRPCticketlisElementFieldBuilder().addBuilder(
          index, de.uniba.rz.io.rpc.gRPCticket.getDefaultInstance());
    }
    /**
     * <code>repeated .gRPCticket gRPCticketlisElement = 1;</code>
     */
    public java.util.List<de.uniba.rz.io.rpc.gRPCticket.Builder> 
         getGRPCticketlisElementBuilderList() {
      return getGRPCticketlisElementFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        de.uniba.rz.io.rpc.gRPCticket, de.uniba.rz.io.rpc.gRPCticket.Builder, de.uniba.rz.io.rpc.gRPCticketOrBuilder> 
        getGRPCticketlisElementFieldBuilder() {
      if (gRPCticketlisElementBuilder_ == null) {
        gRPCticketlisElementBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            de.uniba.rz.io.rpc.gRPCticket, de.uniba.rz.io.rpc.gRPCticket.Builder, de.uniba.rz.io.rpc.gRPCticketOrBuilder>(
                gRPCticketlisElement_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        gRPCticketlisElement_ = null;
      }
      return gRPCticketlisElementBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:gRPCticketlist)
  }

  // @@protoc_insertion_point(class_scope:gRPCticketlist)
  private static final de.uniba.rz.io.rpc.gRPCticketlist DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new de.uniba.rz.io.rpc.gRPCticketlist();
  }

  public static de.uniba.rz.io.rpc.gRPCticketlist getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<gRPCticketlist>
      PARSER = new com.google.protobuf.AbstractParser<gRPCticketlist>() {
    @java.lang.Override
    public gRPCticketlist parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new gRPCticketlist(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<gRPCticketlist> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<gRPCticketlist> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public de.uniba.rz.io.rpc.gRPCticketlist getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

