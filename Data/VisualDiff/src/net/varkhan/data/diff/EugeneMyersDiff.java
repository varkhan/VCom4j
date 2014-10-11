package net.varkhan.data.diff;

import net.varkhan.base.containers.Collection;
import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.array.Arrays;
import net.varkhan.base.containers.list.List;
import net.varkhan.base.containers.list.ArrayList;

import java.util.Comparator;


/**
 * <b></b>.
 * <p/>
 * Eugene Myers: "An O(ND) Difference Algorithm and its Variations", in Algorithmica Vol. 1 No. 2, 1986, p 251.
 *
 * @author varkhan
 * @date 9/21/14
 * @time 3:00 PM
 */
public class EugeneMyersDiff<T, S extends Container<T>, X> implements Diff<T,S,X> {

    protected final Comparator<T> comp;

    public EugeneMyersDiff(Comparator<T> comp) {
        this.comp=comp;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterable<Diff.Block<T>> invoke(S srcL, S srcR, X ctx) {
        Object[] datL=getArray(srcL);
        Object[] datR=getArray(srcR);
        int max = datL.length + datR.length + 1;
        // edits for the begin sequence
        int[] begE=new int[2*max+2];
        // edits for the end sequence
        int[] endE=new int[2*max+2];
        boolean[] edtR = new boolean[datL.length+2];
        boolean[] edtL = new boolean[datR.length+2];
        LCS(datL, edtL, 0, datL.length, datR, edtR, 0, datR.length, begE, endE);
        return GDB(datL, edtL, datR, edtR);
    }

    protected Object[] getArray(S src) {
        Object[] dat = new Object[(int)src.size()];
        int i = 0;
        for(Iterator<? extends T> it=src.iterator();it.hasNext();) {
            dat[i++] = it.next();
        }
        return dat;
    }

    @SuppressWarnings("unchecked")
    protected boolean equals(Object l, Object r) {
        return comp.compare((T)l,(T)r)==0;
    }

    /**
     * An implementation of the longest common-subsequence (LCS) that looks for
     * optimal subsequences anchored at either end of the specified boundaries.
     *
     * @param datL the left-side data
     * @param edtL the left-side edit flags
     * @param begL the left-side start position
     * @param endL the left-side end position
     * @param datR the right-side data
     * @param edtR the right-side edit flags
     * @param begR the right-side start position
     * @param endR the right-side end position
     * @param begE start-side edit sequence
     * @param endE end-side edit sequence
     */
    protected void LCS(Object[] datL, boolean[] edtL, int begL, int endL, Object[] datR, boolean[] edtR, int begR, int endR, int[] begE, int[] endE) {
        // skip identical beg sequences
        while( begL<endL && begR<endR && equals(datL[begL],datR[begR]) ) {
            begL++;
            begR++;
        }
        // skip identical end sequences
        while( begL<endL && begR<endR && equals(datL[endL-1],datR[endR-1]) ) {
            --endL;
            --endR;
        }
        // Insertions and deletions
        if(begL==endL) {
            while(begR<endR) edtR[begR++]=true;
        }
        else if(begR==endR) {
            while(begL<endL) edtL[begL++]=true;
        }
        else {
            // Compute the shortest middle snake (l,r), to get the optimal path
            int[] sms=SMS(datL, begL, endL, datR, begR, endR, begE, endE);

            // The path is from beg to (l,r) and from (l,r) to end
            LCS(datL, edtL, begL, sms[0], datR, edtR, begR, sms[1], begE, endE);
            LCS(datL, edtL, sms[0], endL, datR, edtR, sms[1], endR, begE, endE);
        }
    }

    /**
     * Look for the Shortest Middle Snake between the specified boundaries.
     *
     * @param datL the left-side data
     * @param begL the left-side start position
     * @param endL the left-side end position
     * @param datR the right-side data
     * @param begR the right-side start position
     * @param endR the right-side end position
     * @param begE start-side edit sequence
     * @param endE end-side edit sequence
     * @return the positions of the shortest middle snake
     */
    protected int[] SMS(Object[] datL, int begL, int endL, Object[] datR, int begR, int endR, int[] begE, int[] endE) {

        int max = datL.length+datR.length+1;

        // Beg search starts at this Kline
        int begK = begL-begR;
        // End search starts at this K-line
        int endK = endL-endR;


        // The original algo uses arrays that accepts negative indices.
        // We use 0-based arrays instead, and add respective offsets:
        // beg0 for begE / end0 for endE
        int beg0 = max-begK;
        int end0 = max-endK;

        int difD=(endL-begL)-(endR-begR);
        boolean odd = (difD&1)!=0;
        int maxD= ((endL-begL+endR-begR)/2) + 1;

        // init vectors
        begE[beg0+begK+1]=begL;
        endE[end0+endK-1]=endL;

        for(int d=0; d<=maxD; d++) {

            // Extend the forward path.
            for(int k=begK-d; k<=begK+d; k+=2) {
                // Find the starting point
                int x, y;
                if(k==begK-d) x=begE[beg0+k+1]; // down
                else {
                    x=begE[beg0+k-1]+1; // right
                    if(k<begK+d && begE[beg0+k+1]>=x) x=begE[beg0+k+1]; // down
                }
                y=x-k;
                // Find the end of the furthest reaching forward d-path in diagonal k.
                while( x<endL && y<endR && equals(datL[x],datR[y]) ) {
                    x++;
                    y++;
                }
                begE[beg0+k]=x;

                // overlap ?
                if(odd && endK-d<k && k<endK+d) {
                    if(endE[end0+k] <= begE[beg0+k]) {
                        return new int[] { begE[beg0+k], begE[beg0+k]-k };
                    }
                }
            }

            // Extend the reverse path.
            for(int k=endK-d; k<=endK+d; k+=2) {
                // Find the starting point
                int x, y;
                if(k==endK+d) x=endE[end0+k-1]; // up
                else {
                    x=endE[end0+k+1]-1; // left
                    if(k>endK-d && endE[end0+k-1]<x) x=endE[end0+k-1]; // up
                }
                y=x-k;

                // Find the end of the furthest reaching backward d-path in diagonal k.
                while( x>begL && y>begR && equals(datL[x-1],datR[y-1]) ) {
                    x--;
                    y--;
                }
                endE[end0+k]=x;

                // overlap ?
                if(!odd && begK-d<=k && k<=begK+d) {
                    if(endE[end0+k] <= begE[beg0+k]) {
                        return new int[] { begE[beg0+k], begE[beg0+k]-k };
                    }
                }
            }

        }
        // We should never get there
        throw new RuntimeException("Ran out of possible edits!");
    }


    /**
     * Scan the edit sequences on both sides, start to end, to generate the edit script.
     *
     * @param datL the left-side data
     * @param edtL the left-side edit flags
     * @param datR the right-side data
     * @param edtR the right-side edit flags
     * @return the sequence of diff blocks
     */
    protected Collection<Diff.Block<T>> GDB(Object[] datL, boolean[] edtL, Object[] datR, boolean edtR[]) {
        Collection<Diff.Block<T>> c = new ArrayList<Diff.Block<T>>();
        final List datLa = Arrays.asList(datL);
        final List datRa = Arrays.asList(datR);
        int lenR=datR.length;
        int lenL=datL.length;
        int endL=0;
        int endR=0;
        while(endL<lenL||endR<lenR) {
            // Unchanged
            if(endL<lenL&& !edtL[endL]&&endR<lenR&& !edtR[endR]) {
                endL++;
                endR++;
            }
            // Edited
            else {
                int begL=endL;
                int begR=endR;

                while(endL<lenL&& (endR>=lenR||edtL[endL])) endL++;
                while(endR<lenR&& (endL>=lenL||edtR[endR])) endR++;

                if( begL<endL || begR<endR ) {
                    c.add(new DiffBlock<T>(datLa, begL, endL, datRa, begR, endR));
                }
            }
        }
        return c;
    }


}
