import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static class Node {
        private final Point2D point;
        private final RectHV rect;
        private Node left;
        private Node right;

        public Node(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
        }
    }

    private Node root;
    private int size;

    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        root = insert(root, p, 0, new RectHV(0, 0, 1, 1));
    }

    private Node insert(Node node, Point2D point, int depth, RectHV rect) {
        if (node == null) {
            size++;
            return new Node(point, rect);
        }

        if (node.point.equals(point)) return node;

        boolean isVertical = depth % 2 == 0;

        if (isVertical) {
            if (point.x() < node.point.x()) {
                RectHV leftRect = new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax());
                node.left = insert(node.left, point, depth + 1, leftRect);
            }
            else {
                RectHV rightRect = new RectHV(node.point.x(), rect.ymin(), rect.xmax(),
                                              rect.ymax());
                node.right = insert(node.right, point, depth + 1, rightRect);
            }
        }
        else {
            if (point.y() < node.point.y()) {
                RectHV bottomRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(),
                                               node.point.y());
                node.left = insert(node.left, point, depth + 1, bottomRect);
            }
            else {
                RectHV topRect = new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax());
                node.right = insert(node.right, point, depth + 1, topRect);
            }
        }
        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        return contains(root, p, 0);
    }

    private boolean contains(Node node, Point2D point, int depth) {
        if (node == null) return false;

        if (node.point.equals(point)) return true;

        boolean isVertical = depth % 2 == 0;

        if (isVertical) {
            if (point.x() < node.point.x()) {
                return contains(node.left, point, depth + 1);
            }
            else {
                return contains(node.right, point, depth + 1);
            }
        }
        else {
            if (point.y() < node.point.y()) {
                return contains(node.left, point, depth + 1);
            }
            else {
                return contains(node.right, point, depth + 1);
            }
        }
    }

    public void draw() {
        draw(root, 0);
    }

    private void draw(Node node, int depth) {
        if (node == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();

        boolean isVertical = depth % 2 == 0;

        StdDraw.setPenRadius();
        if (isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }

        draw(node.left, depth + 1);
        draw(node.right, depth + 1);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle cannot be null");
        return range(root, rect, new java.util.ArrayList<>());
    }

    private java.util.ArrayList<Point2D> range(Node node, RectHV rect,
                                               java.util.ArrayList<Point2D> result) {
        if (node == null) return result;

        if (rect.intersects(node.rect)) {
            if (rect.contains(node.point)) result.add(node.point);
            range(node.left, rect, result);
            range(node.right, rect, result);
        }
        return result;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        if (isEmpty()) return null;
        return nearest(root, p, root.point, 0);
    }

    private Point2D nearest(Node node, Point2D target, Point2D closest, int depth) {
        if (node == null) return closest;

        if (node.point.distanceSquaredTo(target) < closest.distanceSquaredTo(target)) {
            closest = node.point;
        }

        boolean isVertical = depth % 2 == 0;

        Node first, second;
        if (isVertical) {
            if (target.x() < node.point.x()) {
                first = node.left;
                second = node.right;
            }
            else {
                first = node.right;
                second = node.left;
            }
        }
        else {
            if (target.y() < node.point.y()) {
                first = node.left;
                second = node.right;
            }
            else {
                first = node.right;
                second = node.left;
            }
        }

        closest = nearest(first, target, closest, depth + 1);

        if (second != null && second.rect.distanceSquaredTo(target) < closest.distanceSquaredTo(
                target)) {
            closest = nearest(second, target, closest, depth + 1);
        }

        return closest;
    }
}
