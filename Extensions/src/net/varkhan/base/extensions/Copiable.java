/**
 * 
 */
package net.varkhan.base.extensions;

/**
 * <b>Specifies means by which the exact state of an object can be copied in a new, independent, instance.</b>
 * <p/>
 * A substitute for the {@link Cloneable} interface, that also differentiates
 * between shallow and recursive copy.
 * <p/>
 * @param <Type> the type of the object being made copiable
 * @author varkhan
 * @date Apr 3, 2009
 * @time 11:17:16 PM
 */
public interface Copiable<Type> {

	/**
	 * Makes a copy of the specified object.
	 * <p/>
	 * The precise meaning  of "copy" may depend on the class of the object.
	 * It is however required that for any object {@code o}, the expressions
     * {@code o.softCopy() != o}, {@code o.softCopy().getClass() == o.getClass()} and {@code o.softCopy().equals(o)}
     * evaluate to {@literal true}.
	 * 
	 * @param o
	 * @return
	 */
	public Type softCopy(Type o);
	
	/**
	 * Makes a copy of the specified object, and recursively of all of its fields.
	 * <p/>
	 * The precise meaning  of "copy" may depend on the class of the object.
	 * It is however required that for any object {@code o}, the expressions
     * {@code o.hardCopy() != o}, {@code o.hardCopy().getClass() == o.getClass()} and {@code o.hardCopy().equals(o)}
     * evaluate to {@literal true}.
	 * 
	 * @param o
	 * @return
	 */
	public Type hardCopy(Type o);
	
}
