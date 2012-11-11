package net.varkhan.base.management.logging.decorate;

import javax.management.DescriptorKey;
import java.lang.annotation.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/20/12
 * @time 7:47 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Loggable {

    @Target( { ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Field {
        @DescriptorKey("Name") String name();
    }

    @Target( { ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Comment {
        @DescriptorKey("Comment") String comment();
    }

    @Target( { ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Nullable {
        @DescriptorKey("AllowsNulls") boolean value() default true;
    }

}
