package functions;

import java.io.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {

    private static class FunctionNode {
        private FunctionPoint data;
        private FunctionNode prev = null;
        private FunctionNode next = null;

        public FunctionNode(FunctionPoint data) {
            this.data = data;
        }

        public FunctionNode() {
            this.data = null;
        }

        public FunctionNode(FunctionPoint data, FunctionNode prev, FunctionNode next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public FunctionPoint getData() { return data; }
        public void setData(FunctionPoint data) { this.data = data; }
        public FunctionNode getPrev() { return prev; }
        public void setPrev(FunctionNode prev) { this.prev = prev; }
        public FunctionNode getNext() { return next; }
        public void setNext(FunctionNode next) { this.next = next; }
    }

    private FunctionNode head;
    private int size;
    private FunctionNode lastNode;
    private int lastIndex;


    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2) {
            throw new IllegalArgumentException("The number of elements is less than 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException(leftX + " bigger than " + rightX);
        }

        initializeEmptyList();

        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            addNodeToTail(new FunctionPoint(x, 0));
        }
    }

    public LinkedListTabulatedFunction(){};

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (values.length < 2) {
            throw new IllegalArgumentException("The number of elements is less than 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException(leftX + " bigger than " + rightX);
        }

        System.out.println("начало конструктора, values.length = " + values.length);

        initializeEmptyList();

        double step = (rightX - leftX) / (values.length- 1);

        System.out.println("size = " + values.length + ", step = " + step);

        for (int i = 0; i < values.length; i++) {
            System.out.println("i = " + i + ", values[i] = " + values[i]);
            double x = leftX + i * step;
            System.out.println("создание точки x = " + x + ", y = " + values[i]);
            addNodeToTail(new FunctionPoint(x, values[i]));
        }

        System.out.println("бетте");
    }

    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("At least 2 points are required");
        }

        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i - 1].getX()) {
                throw new IllegalArgumentException("Points must be strictly increasing by X coordinate");
            }
        }

        initializeEmptyList();
        for (FunctionPoint point : points) {
            addNodeToTail(new FunctionPoint(point.getX(), point.getY()));
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(size);

        FunctionNode current = head.getNext();
        while (current != head) {
            out.writeDouble(current.getData().getX());
            out.writeDouble(current.getData().getY());
            current = current.getNext();
        }
    }

    public void readExternal(ObjectInput in) throws IOException{
        initializeEmptyList();

        int pointsCount = in.readInt();

        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            addNodeToTail(new FunctionPoint(x, y));
        }
    }


    private void initializeEmptyList() {
        head = new FunctionNode();
        head.setNext(head);
        head.setPrev(head);
        size = 0;
        lastNode = head;
        lastIndex = -1;
    }


    private FunctionNode addNodeToTail(FunctionPoint point) {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null");
        }

        FunctionNode newNode = new FunctionNode(point);
        FunctionNode lastNode = head.getPrev();


        newNode.setPrev(lastNode);
        newNode.setNext(head);


        lastNode.setNext(newNode);
        head.setPrev(newNode);

        size++;
        this.lastNode = newNode;
        lastIndex = size - 1;

        return newNode;
    }


    public double getFunctionValue(double x) {
        if (x < this.getLeftDomainBorder() || x > this.getRightDomainBorder()) {
            return Double.NaN;
        }

        FunctionNode current = this.head.getNext();
        while (current != head && x > current.getData().getX()) {
            current = current.getNext();
        }

        if (current == head) {

            current = head.getPrev();
        }

        if (Math.abs(current.getData().getX() - x) < 1e-9) {
            return current.getData().getY();
        }

        FunctionNode prevNode = current.getPrev();
        if (prevNode == head) {
            prevNode = head.getNext();
        }

        double x1 = prevNode.getData().getX();
        double y1 = prevNode.getData().getY();
        double x2 = current.getData().getX();
        double y2 = current.getData().getY();

        return y1 + ((x - x1) * (y2 - y1)) / (x2 - x1);
    }

    public double getLeftDomainBorder() {
        if (size == 0) return Double.NaN;
        return this.head.getNext().getData().getX();
    }

    public double getRightDomainBorder() {
        if (size == 0) return Double.NaN;
        return this.head.getPrev().getData().getX();
    }

    public int getPointsCount() {
        return size;
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }
        FunctionNode node = getNodeByIndex(index);
        return new FunctionPoint(node.getData().getX(), node.getData().getY());
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }

        double newX = point.getX();
        FunctionNode currentNode = getNodeByIndex(index);

        if (index > 0) {
            FunctionNode prevNode = currentNode.getPrev();
            if (prevNode != head && newX <= prevNode.getData().getX()) {
                throw new InappropriateFunctionPointException(
                        "X coordinate " + newX + " must be greater than previous point's X: " + prevNode.getData().getX()
                );
            }
        }

        if (index < size - 1) {
            FunctionNode nextNode = currentNode.getNext();
            if (nextNode != head && newX >= nextNode.getData().getX()) {
                throw new InappropriateFunctionPointException(
                        "X coordinate " + newX + " must be less than next point's X: " + nextNode.getData().getX()
                );
            }
        }

        currentNode.setData(new FunctionPoint(newX, point.getY()));
    }

    public double getPointX(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }
        return getNodeByIndex(index).getData().getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        FunctionNode prev = node.getPrev();
        if (prev != head && x <= prev.getData().getX()) {
            throw new InappropriateFunctionPointException("X must be greater than previous point");
        }

        FunctionNode next = node.getNext();
        if (next != head && x >= next.getData().getX()) {
            throw new InappropriateFunctionPointException("X must be less than next point");
        }

        node.setData(new FunctionPoint(x, node.getData().getY()));
    }

    public double getPointY(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }
        return getNodeByIndex(index).getData().getY();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }
        getNodeByIndex(index).getData().setY(y);
    }

    public void deletePoint(int index) {
        if (size < 3) {
            throw new IllegalStateException("Cannot delete point: minimum 2 points required");
        }
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null");
        }

        double x = point.getX();
        FunctionNode current = head.getNext();
        int insertIndex = 0;

        while (current != head) {
            double currentX = current.getData().getX();

            if (Math.abs(currentX - x) < 1e-9) {
                throw new InappropriateFunctionPointException(
                        "Point with X=" + x + " already exists at index " + insertIndex
                );
            }

            if (currentX > x) {
                break;
            }
            current = current.getNext();
            insertIndex++;
        }

        addNodeByIndex(insertIndex, new FunctionPoint(point));
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }

        if (lastNode != null && lastIndex == index && lastNode != head) {
            return lastNode;
        }

        FunctionNode current;
        if (index < size / 2) {
            current = head.getNext();
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = head.getPrev();
            for (int i = size - 1; i > index; i--) {
                current = current.getPrev();
            }
        }

        lastNode = current;
        lastIndex = index;
        return current;
    }

    private FunctionNode addNodeByIndex(int index, FunctionPoint point) {
        if (index < 0 || index > size) {
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }

        if (index == size) {
            return addNodeToTail(point);
        }

        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode prevNode = nextNode.getPrev();

        FunctionNode newNode = new FunctionNode(point);
        newNode.setPrev(prevNode);
        newNode.setNext(nextNode);

        prevNode.setNext(newNode);
        nextNode.setPrev(newNode);

        size++;
        lastNode = newNode;
        lastIndex = index;

        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("out of range");
        }
        if (size <= 2) {
            throw new IllegalStateException("Cannot delete point: minimum 2 points required");
        }

        FunctionNode nodeToDelete = getNodeByIndex(index);
        FunctionNode prevNode = nodeToDelete.getPrev();
        FunctionNode nextNode = nodeToDelete.getNext();

        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);

        nodeToDelete.setPrev(null);
        nodeToDelete.setNext(null);

        size--;
        lastNode = null;
        lastIndex = -1;

        return nodeToDelete;
    }
}