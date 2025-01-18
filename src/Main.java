import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

    static String apiKey = "c28f1d19e7d243488ed43558251801";

    public static String getWeatherData(String city) {
        String url = "https://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + city;
        StringBuilder result = new StringBuilder();

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            return "Error fetching data!";
        }
        return result.toString();
    }

    public static String extractWeatherCondition(String weatherData) {
        try {
            int conditionStart = weatherData.indexOf("\"text\":\"") + 8;
            int conditionEnd = weatherData.indexOf("\"", conditionStart);
            return weatherData.substring(conditionStart, conditionEnd);
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public static double extractTemperature(String weatherData) {
        try {
            int tempStart = weatherData.indexOf("\"temp_c\":") + 9;
            int tempEnd = weatherData.indexOf(",", tempStart);
            return Double.parseDouble(weatherData.substring(tempStart, tempEnd));
        } catch (Exception e) {
            return 0;
        }
    }

    public static double extractWindSpeed(String weatherData) {
        try {
            int windStart = weatherData.indexOf("\"wind_mph\":") + 11;
            int windEnd = weatherData.indexOf(",", windStart);
            return Double.parseDouble(weatherData.substring(windStart, windEnd));
        } catch (Exception e) {
            return 0;
        }
    }

    public static int extractHumidity(String weatherData) {
        try {
            int humidityStart = weatherData.indexOf("\"humidity\":") + 11;
            int humidityEnd = weatherData.indexOf(",", humidityStart);
            return Integer.parseInt(weatherData.substring(humidityStart, humidityEnd));
        } catch (Exception e) {
            return 0;
        }
    }

    public static double extractPressure(String weatherData) {
        try {
            int pressureStart = weatherData.indexOf("\"pressure_mb\":") + 14;
            int pressureEnd = weatherData.indexOf(",", pressureStart);
            return Double.parseDouble(weatherData.substring(pressureStart, pressureEnd));
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getAdvice(String condition, double temperature, double windSpeed, int humidity, double pressure) {
        StringBuilder advice = new StringBuilder();

        if (temperature > 30) {
            advice.append("Very hot! Stay hydrated, wear light clothes. ");
        } else if (temperature < 10) {
            advice.append("Cold! Dress warmly. ");
        } else {
            advice.append("Moderate temperature. Enjoy your day! ");
        }

        if (windSpeed > 20) {
            advice.append("Strong wind! Be cautious of debris. ");
        }

        if (condition.contains("Rain")) {
            advice.append("It's raining! Carry an umbrella. ");
        } else if (condition.contains("Snow")) {
            advice.append("Snowing! Wear warm clothes. ");
        } else if (condition.contains("Cloud")) {
            advice.append("Cloudy. Bring a jacket. ");
        } else if (condition.contains("Clear")) {
            advice.append("Clear skies! Great for outdoor activities. ");
        }

        if (humidity > 80) {
            advice.append("High humidity. Stay cool. ");
        } else if (humidity < 30) {
            advice.append("Low humidity. Moisturize your skin. ");
        }

        if (pressure < 1010) {
            advice.append("Low pressure. Stormy weather possible. ");
        } else if (pressure > 1020) {
            advice.append("High pressure. Calm weather ahead. ");
        }

        return advice.toString();
    }

    public static String getSuggestions(double temperature, double windSpeed, int humidity, String condition) {
        StringBuilder suggestion = new StringBuilder();

        if (condition.contains("Rain")) {
            suggestion.append("Consider staying indoors or carrying an umbrella. ");
        } else if (condition.contains("Snow")) {
            suggestion.append("Stay warm and consider staying indoors. ");
        } else if (condition.contains("Clear")) {
            if (temperature > 25) {
                suggestion.append("Great day for outdoor activities! Enjoy the sunshine. ");
            } else if (temperature > 10) {
                suggestion.append("Perfect day for a walk. ");
            } else {
                suggestion.append("Nice day to go outside but dress warmly. ");
            }
        } else {
            suggestion.append("Unpredictable weather, stay prepared! ");
        }

        if (windSpeed > 20) {
            suggestion.append("High wind speeds. Be cautious if going outside. ");
        }

        if (humidity > 80) {
            suggestion.append("High humidity. Consider staying indoors with air conditioning. ");
        }

        if (temperature > 30) {
            suggestion.append("Extreme heat! Drink plenty of water and avoid being outside for long periods. ");
        }

        return suggestion.toString();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Weather Application");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel cityLabel = new JLabel("Enter City:");
        cityLabel.setBounds(50, 50, 100, 30);
        frame.add(cityLabel);

        JTextField cityField = new JTextField();
        cityField.setBounds(150, 50, 200, 30);
        frame.add(cityField);

        JLabel weatherLabel = new JLabel("Weather Info:");
        weatherLabel.setBounds(50, 100, 400, 30);
        frame.add(weatherLabel);

        JLabel tempLabel = new JLabel("Temperature (°C):");
        tempLabel.setBounds(50, 130, 400, 30);
        frame.add(tempLabel);

        JLabel windLabel = new JLabel("Wind Speed (mph):");
        windLabel.setBounds(50, 160, 400, 30);
        frame.add(windLabel);

        JLabel humidityLabel = new JLabel("Humidity (%):");
        humidityLabel.setBounds(50, 190, 400, 30);
        frame.add(humidityLabel);

        JLabel pressureLabel = new JLabel("Pressure (mb):");
        pressureLabel.setBounds(50, 220, 400, 30);
        frame.add(pressureLabel);

        JLabel adviceLabel = new JLabel("Advice:");
        adviceLabel.setBounds(50, 250, 400, 60);
        adviceLabel.setVerticalAlignment(SwingConstants.TOP);
        frame.add(adviceLabel);

        JLabel suggestionLabel = new JLabel("Suggestions:");
        suggestionLabel.setBounds(50, 320, 400, 60);
        suggestionLabel.setVerticalAlignment(SwingConstants.TOP);
        frame.add(suggestionLabel);

        JButton getWeatherButton = new JButton("Get Weather");
        getWeatherButton.setBounds(200, 280, 120, 30);
        frame.add(getWeatherButton);

        getWeatherButton.addActionListener(e -> {
            String city = cityField.getText();
            if (!city.isEmpty()) {
                String weatherData = getWeatherData(city);
                if (weatherData.equals("Error fetching data!")) {
                    weatherLabel.setText("Error: Unable to fetch data.");
                    tempLabel.setText("");
                    windLabel.setText("");
                    humidityLabel.setText("");
                    pressureLabel.setText("");
                    adviceLabel.setText("Please check the city name and try again.");
                    suggestionLabel.setText("");
                } else {
                    String condition = extractWeatherCondition(weatherData);
                    double temperature = extractTemperature(weatherData);
                    double windSpeed = extractWindSpeed(weatherData);
                    int humidity = extractHumidity(weatherData);
                    double pressure = extractPressure(weatherData);

                    weatherLabel.setText("Condition: " + condition);
                    tempLabel.setText("Temperature: " + temperature + " °C");
                    windLabel.setText("Wind Speed: " + windSpeed + " mph");
                    humidityLabel.setText("Humidity: " + humidity + " %");
                    pressureLabel.setText("Pressure: " + pressure + " mb");
                    adviceLabel.setText("<html>Advice: " + getAdvice(condition, temperature, windSpeed, humidity, pressure) + "</html>");
                    suggestionLabel.setText("<html>Suggestions: " + getSuggestions(temperature, windSpeed, humidity, condition) + "</html>");
                }
            } else {
                weatherLabel.setText("Please enter a city.");
                tempLabel.setText("");
                windLabel.setText("");
                humidityLabel.setText("");
                pressureLabel.setText("");
                adviceLabel.setText("");
                suggestionLabel.setText("");
            }
        });

        frame.setVisible(true);
    }
}
