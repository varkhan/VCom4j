package net.varkhan.base.extensions;

import junit.framework.TestCase;

@SuppressWarnings("unchecked")
public class LabelTest extends TestCase {

    protected static class L0 implements Label<L0> {
        @Override public String toString() { return this.getClass().getSimpleName(); }
    }
    protected static final L0 l0 = new L0();

    protected static class L1 implements Label<L1> {
        @Override public boolean isOverriddenBy(L1 label) { return label instanceof L11 || label instanceof L12; }
        @Override public String toString() { return this.getClass().getSimpleName(); }
    }
    protected static class L11 extends L1 implements Label<L1> {
        @Override public boolean isOverriddenBy(L1 label) { return false; }
    }
    protected static class L12 extends L1 implements Label<L1> {
        @Override public boolean isOverriddenBy(L1 label) { return label instanceof L13; }
    }
    protected static class L13 extends L12 implements Label<L1> {
        @Override public boolean isOverriddenBy(L1 label) { return false; }
    }
    protected static final L1 l10 = new L1();
    protected static final L1 l11 = new L11();
    protected static final L1 l12 = new L12();
    protected static final L1 l13 = new L13();

    public void testSetOf() {
        assertTrue("isEmpty",Label.setOf().isEmpty());
        assertTrue("contains",Label.setOf(l0).contains(l0));
        assertFalse("contains",Label.setOf(l11).contains(l12));
    }

    public void testIsOverriding() {
        assertFalse("!isOverriding(l,{})",Label.isOverriding(l0,Label.setOf()));
        assertFalse("!isOverriding(l,{l})",Label.isOverriding(l0,Label.setOf(l0)));
        assertFalse("!isOverriding(l,{<l})",Label.isOverriding(l12,Label.setOf(l13)));
        assertFalse("!isOverriding(l,{!=l})",Label.isOverriding(l12,Label.setOf(l11)));
        assertTrue("isOverriding(l,{>l})",Label.isOverriding(l13,Label.setOf(l12)));
        assertTrue("isOverriding(l,{>l,!=l})",Label.isOverriding(l13,Label.setOf(l12,l11)));
        assertFalse("!isOverriding(l,{<l,!=l})",Label.isOverriding(l12,Label.setOf(l11,l13)));
        assertTrue("isOverriding(l,{>l,!=l,<l})",Label.isOverriding(l12,Label.setOf(l10,l11,l13)));
    }

    public void testIsOverridden() {
        assertFalse("!isOverridden(l,{})",Label.isOverridden(l0,Label.setOf()));
        assertFalse("!isOverridden(l,{l})",Label.isOverridden(l0,Label.setOf(l0)));
        assertTrue("isOverridden(l,{<l})",Label.isOverridden(l12,Label.setOf(l13)));
        assertFalse("!isOverridden(l,{!=l})",Label.isOverridden(l12,Label.setOf(l11)));
        assertFalse("!isOverridden(l,{>l})",Label.isOverridden(l13,Label.setOf(l12)));
        assertFalse("!isOverridden(l,{>l,!=l})",Label.isOverridden(l13,Label.setOf(l12,l11)));
        assertTrue("isOverridden(l,{<l,!=l})",Label.isOverridden(l12,Label.setOf(l11,l13)));
        assertTrue("isOverridden(l,{>l,!=l,<l})",Label.isOverridden(l12,Label.setOf(l10,l11,l13)));
    }

    public void testExcludeOverriding() {
        assertEquals("",Label.setOf(l0),Label.excludeOverriding(Label.setOf(l0),Label.setOf()));
        assertEquals("",Label.setOf(),Label.excludeOverriding(Label.setOf(),Label.setOf(l0)));
        assertEquals("",Label.setOf(l11,l12,l13),Label.excludeOverriding(Label.setOf(l11,l12,l13),Label.setOf(l13)));
        assertEquals("",Label.setOf(l11,l12),Label.excludeOverriding(Label.setOf(l11,l12,l13),Label.setOf(l12)));
    }

    public void testExcludeOverridden() {
        assertEquals("",Label.setOf(l0),Label.excludeOverridden(Label.setOf(l0),Label.setOf()));
        assertEquals("",Label.setOf(),Label.excludeOverridden(Label.setOf(),Label.setOf(l0)));
        assertEquals("",Label.setOf(l0),Label.excludeOverridden(Label.setOf(l0),Label.setOf(l0)));
        assertEquals("",Label.setOf(l11,l13),Label.excludeOverridden(Label.setOf(l11,l12,l13),Label.setOf(l13)));
        assertEquals("",Label.setOf(l11,l12,l13),Label.excludeOverridden(Label.setOf(l11,l12,l13),Label.setOf(l12)));
    }

    public void testOverriddenBound() {
        assertEquals("",Label.setOf(l0),Label.overriddenBound(Label.setOf(l0)));
        assertEquals("",Label.setOf(l11,l12),Label.overriddenBound(Label.setOf(l11,l12,l13)));
        assertEquals("",Label.setOf(l10),Label.overriddenBound(Label.setOf(l10,l11,l12,l13)));
    }

    public void testOverridingBound() {
        assertEquals("",Label.setOf(l0),Label.overridingBound(Label.setOf(l0)));
        assertEquals("",Label.setOf(l11,l13),Label.overridingBound(Label.setOf(l11,l12,l13)));
        assertEquals("",Label.setOf(l11,l13),Label.overridingBound(Label.setOf(l10,l11,l12,l13)));
    }
}