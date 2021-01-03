package com.lh.rpccore.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

@Slf4j
public class Object2Array {

    /**
     * 对象转Byte数组.
     *
     * @param object object.
     * @return byte[]
     * @throws IOException e
     */
    public static byte[] objectToByteArray(final Object object) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("对象转Byte数组失败 " + e.getMessage());
            e.printStackTrace();
        } finally {
            Objects.requireNonNull(objectOutputStream).close();
            byteArrayOutputStream.close();
        }

        return bytes;
    }

    /**
     * Byte数组转对象.
     *
     * @param bytes byte[]
     * @return Object
     * @throws IOException e
     */
    public static Object byteArrayToObject(final byte[] bytes) throws IOException {
        Object object = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            object = objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Byte数组转对象失败 " + e.getMessage());
            e.printStackTrace();
        } finally {
            Objects.requireNonNull(objectInputStream).close();
            byteArrayInputStream.close();
        }

        return object;
    }
}
