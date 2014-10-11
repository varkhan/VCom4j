package net.varkhan.data.diff;

import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.list.List;


/**
* <b>A basic implementation of a diff blocks</b>.
* <p/>
*
* @author varkhan
* @date 10/5/14
* @time 2:03 PM
*/
public class DiffBlock<T> implements Diff.Block<T> {
    protected int          begL;
    protected int          begR;
    protected int          endL;
    protected int          endR;
    protected Container<T> blockL;
    protected Container<T> blockR;

    @SuppressWarnings("unchecked")
    public DiffBlock(List datL, int begL, int endL, List datR, int begR, int endR) {
        this.begL=begL;
        this.begR=begR;
        this.endL=endL;
        this.endR=endR;
        blockL = datL.sublist(begL, endL);
        blockR = datR.sublist(begR, endR);
    }

    @Override
    public int begL() { return begL; }

    @Override
    public int begR() { return begR; }

    @Override
    public int endL() { return endL; }

    @Override
    public int endR() { return endR; }

    @Override
    public Container<T> blockL() { return blockL; }

    @Override
    public Container<T> blockR() { return blockR; }

    @Override
    public String toString() { return "[ "+begL+","+endL+" | "+begR+","+endR+" ]"; }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(!(o instanceof Diff.Block)) return false;
        Diff.Block b = (Diff.Block) o;
        return begL==b.begL() && begR==b.begR() && endL==b.endL() && endR==b.endR();
    }

    @Override
    public int hashCode() {
        int result=begL;
        result=31*result+begR;
        result=31*result+endL;
        result=31*result+endR;
        return result;
    }

}
