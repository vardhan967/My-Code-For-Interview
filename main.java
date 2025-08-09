import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) throws Exception {
        // Process first file
        processFile("input1.json");

        // Process second file
        processFile("input2.json");
    }

    static void processFile(String filename) throws IOException {
        // Read JSON from file
        String jsonText = new String(Files.readAllBytes(Paths.get(filename)));
        JSONObject obj = new JSONObject(jsonText);

        // Get n and k
        JSONObject keys = obj.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        List<BigInteger> xs = new ArrayList<>();
        List<BigInteger> ys = new ArrayList<>();

        // Read and decode coordinates
        for (String key : obj.keySet()) {
            if (key.equals("keys")) continue;
            JSONObject point = obj.getJSONObject(key);
            String baseStr = point.getString("base");
            String valueStr = point.getString("value");
            BigInteger x = new BigInteger(key);
            BigInteger y = new BigInteger(valueStr, Integer.parseInt(baseStr));
            xs.add(x);
            ys.add(y);
        }

        // Use first k points for interpolation
        BigInteger c = lagrangeAtZero(xs.subList(0, k), ys.subList(0, k));

        // Output result
        System.out.println("File: " + filename);
        System.out.println("{\"c\": " + c.toString() + "}");
    }

    // Lagrange interpolation at x = 0
    static BigInteger lagrangeAtZero(List<BigInteger> xs, List<BigInteger> ys) {
        BigInteger sum = BigInteger.ZERO;
        int size = xs.size();

        for (int j = 0; j < size; j++) {
            BigInteger num = ys.get(j);
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int m = 0; m < size; m++) {
                if (m == j) continue;
                numerator = numerator.multiply(xs.get(m).negate());
                denominator = denominator.multiply(xs.get(j).subtract(xs.get(m)));
            }

            // num * numerator / denominator
            BigInteger term = num.multiply(numerator).divide(denominator);
            sum = sum.add(term);
        }
        return sum;
    }
}
