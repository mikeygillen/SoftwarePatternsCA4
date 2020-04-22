package com.example.softwarepatternsca4.Interfaces;

public class PriceRepository implements Container, Interface{

    @Override
    public Iterator getIterator() {
        return new PriceIterator();
    }

    private class PriceIterator implements Iterator {
        int index;
        @Override
        public boolean hasNext() {

            if(index < shoppingList.size()){
                return true;
            }
            return false;
        }

        @Override
        public Object next() {
            if(this.hasNext()){
                return shoppingList.get(index++);
            }
            return null;
        }
    }
}
