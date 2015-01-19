package net.varkhan.serv.p2p.connect.protocol;

import net.varkhan.base.conversion.serializer.Serializer;
import net.varkhan.base.conversion.serializer.primitives.StringSerializer;
import net.varkhan.base.conversion.serializer.primitives.VariadicSerializer;
import net.varkhan.serv.p2p.connect.PeerProperties;
import net.varkhan.serv.p2p.connect.config.MapProperties;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 10:14 AM
 */
public class BinaryPropertiesSerializer<C> implements Serializer<PeerProperties, C> {

    public MapProperties decode(InputStream stm, C ctx) {
        MapProperties props = new MapProperties();
        long rnum=VariadicSerializer._decode(stm);
        PeerProperties.Realm[] realms=PeerProperties.Realm.values();
        for(int r=0;r<rnum&&r<realms.length;r++) {
            PeerProperties.Realm realm =realms[r];
            long size=VariadicSerializer._decode(stm);
            for(long i=0;i<size;i++) {
                String key=StringSerializer._decode(stm);
                String val=StringSerializer._decode(stm);
                props.setProperty(realm, key, val);
            }
        }
        return props;
    }

    public MapProperties decode(ByteBuffer buf, C ctx) {
        MapProperties props = new MapProperties();
        long rnum=VariadicSerializer._decode(buf);
        PeerProperties.Realm[] realms=PeerProperties.Realm.values();
        for(int r=0;r<rnum&&r<realms.length;r++) {
            PeerProperties.Realm realm =realms[r];
            long size=VariadicSerializer._decode(buf);
            for(long i=0;i<size;i++) {
                String key=StringSerializer._decode(buf);
                String val=StringSerializer._decode(buf);
                props.setProperty(realm, key, val);
            }
        }
        return props;
    }

    public MapProperties decode(byte[] dat, long pos, long len, C ctx) {
        MapProperties props = new MapProperties();
        long rnum=VariadicSerializer._decode(dat, pos, len);
        pos+=VariadicSerializer._length(rnum);
        PeerProperties.Realm[] realms=PeerProperties.Realm.values();
        for(int r=0;r<rnum&&r<realms.length;r++) {
            PeerProperties.Realm realm =realms[r];
            long size=VariadicSerializer._decode(dat, pos, len);
            pos+=VariadicSerializer._length(size);
            for(long i=0;i<size;i++) {
                String key=StringSerializer._decode(dat, pos, len);
                pos+=StringSerializer._length(key);
                String val=StringSerializer._decode(dat, pos, len);
                pos+=StringSerializer._length(val);
                props.setProperty(realm, key, val);
            }
        }
        return props;
    }

    public long encode(PeerProperties obj, OutputStream stm, C ctx) {
        PeerProperties.Realm[] realms=PeerProperties.Realm.values();
        long c=VariadicSerializer._encode(realms.length, stm);
        for(PeerProperties.Realm realm : realms) {
            PeerProperties p=obj.getProperties(realm);
            for(String key : p.getPropertyNames()) {
                c+=StringSerializer._encode(key, stm);
                c+=StringSerializer._encode(p.getProperty(key), stm);
            }
        }
        return c;
    }

    public long encode(PeerProperties obj, ByteBuffer buf, C ctx) {
        PeerProperties.Realm[] realms=PeerProperties.Realm.values();
        long c=VariadicSerializer._encode(realms.length, buf);
        for(PeerProperties.Realm realm : realms) {
            PeerProperties p=obj.getProperties(realm);
            for(String key : p.getPropertyNames()) {
                c+=StringSerializer._encode(key, buf);
                c+=StringSerializer._encode(p.getProperty(key), buf);
            }
        }
        return c;
    }

    public long encode(PeerProperties obj, byte[] dat, long pos, long len, C ctx) {
        PeerProperties.Realm[] realms=PeerProperties.Realm.values();
        long c=VariadicSerializer._encode(realms.length, dat, pos, len);
        for(PeerProperties.Realm realm : realms) {
            PeerProperties p=obj.getProperties(realm);
            for(String key : p.getPropertyNames()) {
                c+=StringSerializer._encode(key, dat, pos+c, len-c);
                c+=StringSerializer._encode(p.getProperty(key), dat, pos+c, len-c);
            }
        }
        return c;
    }

    public long length(PeerProperties obj, C ctx) {
        PeerProperties.Realm[] realms=PeerProperties.Realm.values();
        long c=VariadicSerializer._length(realms.length);
        for(PeerProperties.Realm realm : realms) {
            PeerProperties p=obj.getProperties(realm);
            for(String key : p.getPropertyNames()) {
                c+=StringSerializer._length(key);
                c+=StringSerializer._length(p.getProperty(key));
            }
        }
        return c;
    }

}
