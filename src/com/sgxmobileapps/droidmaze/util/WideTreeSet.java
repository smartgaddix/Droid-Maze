/**
 * Copyright 2011 Massimo Gaddini
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  
 */
package com.sgxmobileapps.droidmaze.util;

import java.util.TreeSet;

/**
 * @author Massimo Gaddini
 * 
 */
public class WideTreeSet<T extends Comparable<T>> {

    protected static long LEVEL_WIDTH_THRESHOLD = 100;

    class SetItem<T extends Comparable<T>> implements Comparable<SetItem<T>> {

        private T              mItem;
        private WideTreeSet<T> mItemSet;

        SetItem(T item, WideTreeSet<T> set) {
            mItem = item;
            mItemSet = set;
        }

        public int compareTo(SetItem<T> another) {
            return mItem.compareTo(another.mItem);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            return mItem.equals( ( (SetItem<T>) obj ).mItem);
        }

    }

    private SetItem<T>          mPrime;
    private TreeSet<SetItem<T>> mLeafs = new TreeSet<SetItem<T>>();

    /**
	 * 
	 */
    public WideTreeSet() {

    }

    public void add(T elem) {
        SetItem<T> item = new SetItem<T>(elem, this);

        if (mPrime == null) {
            mPrime = item;
        }

        mLeafs.add(new SetItem<T>(elem, this));
    }

    public void join(WideTreeSet<T> otherSet) {

    }

    public boolean isSameSet(WideTreeSet<T> otherSet) {
        return false;
    }

    public long getSize() {
        return 0;
    }
}
