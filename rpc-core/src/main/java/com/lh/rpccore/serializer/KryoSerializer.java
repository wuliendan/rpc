package com.lh.rpccore.serializer;

import com.esotericsoftware.kryo.Kryo;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.lh.rpccore.entity.RPCRequest;
import com.lh.rpccore.entity.RPCResponse;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.serializer.SerializerException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Kryo 序列化类，Kryo 序列化效率很高，但是只兼容 Java 语言.
 */
@Slf4j
public class KryoSerializer {

    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RPCResponse.class);
        kryo.register(RPCRequest.class);
        // 默认值为true，是否关闭注册行为，关闭以后可能存在序列化问题，一般推荐设置为 true
        kryo.setReferences(true);
        //默认值为 false，是否关闭循环引用，可以提高性能，但是一般不推荐设置为 true
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    /**
     * 序列化.
     *
     * @param obj obj
     * @return return
     */
    public byte[] serialize(final Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeClassAndObject(output, obj);
            kryoThreadLocal.remove();
            output.close();
            return output.toBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SerializerException("序列化失败");
        }
    }

    /**
     * 反序列化.
     *
     * @param bytes bytes
     * @param <T> T
     * @param clazz clazz
     * @return T
     */
    public <T> T deserialize(final byte[] bytes, final Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            //byte -> Object：从 byte 数据中反序列化出对对象
            Object object = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            input.close();
            return clazz.cast(object);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SerializerException("反序列化失败");
        }
    }
}
