package com.example.zachet1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    TextView _resultField;
    EditText _numberField;
    TextView _operationField;
    Double _operand = null;
    String _lastOperation = "=";
    final String FILE_NAME = "savedResult.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _resultField = findViewById(R.id.result);
        _resultField.setText("0.0");
        _numberField = findViewById(R.id.number);
        _operationField = findViewById(R.id.operation);
    }

    public void onNumberClick(View view)
    {
        Button button = (Button) view;
        _numberField.append(button.getText());

        if ((_lastOperation.equals("=")) && (_operand != null))
        {
            _operand = null;
        }
    }

    public void onOperationClick(View view)
    {
        Button button = (Button) view;
        String inputOperation = button.getText().toString();
        String number = _numberField.getText().toString();
        System.out.println(number);
        if (number.length() > 0)
        {
            try
            {
                performOperation(Double.valueOf(number), inputOperation);
            } catch (NumberFormatException ex)
            {
                _numberField.setText("");
            }
        }
        _lastOperation = inputOperation;
        _operationField.setText(_lastOperation);
    }

    private void performOperation(Double number, String operation)
    {
        if (_operand == null)
        {
            _operand = number;
        } else
        {
            if (_lastOperation.equals("="))
            {
                _lastOperation = operation;
            }
            switch (_lastOperation)
            {
                case "=":
                    _operand = number;
                    break;
                case "/":
                    if (number == 0)
                    {
                        _operand = 0.0;
                    } else
                    {
                        _operand /= number;
                    }
                    break;
                case "*":
                    _operand *= number;
                    break;
                case "+":
                    _operand += number;
                    break;
                case "-":
                    _operand -= number;
                    break;
            }
        }
        _resultField.setText(_operand.toString());
        _numberField.setText("");
    }

    public void clearData(View view)
    {
        _operand = 0.0;
        _lastOperation = "=";
        _resultField.setText(_operand.toString());
        _operationField.setText(_lastOperation);
        _numberField.setText("");
    }

    private File getExternalPath()
    {
        return new File(getExternalFilesDir(null), FILE_NAME);
    }

    public void onSaveClick(View view)
    {
        try (FileOutputStream fos = new FileOutputStream(getExternalPath()))
        {
            String text = _resultField.getText().toString();
            fos.write(text.getBytes());
            Toast.makeText(this, "Значение сохранено", Toast.LENGTH_SHORT).show();
        } catch (IOException ex)
        {
            Toast.makeText(this, "Ошибка при записи файла", Toast.LENGTH_SHORT).show();
        }
    }

    public void onOpenClick(View view)
    {
        File file = getExternalPath();
        if (!file.exists())
        {
            Toast.makeText(this, "Запись не найдена", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (_operationField.getText().equals("="))
            {
                _operationField.setText("+");
                _lastOperation = "+";
            }
            try (FileInputStream fin = new FileInputStream(file))
            {
                byte[] bytes = new byte[fin.available()];
                fin.read(bytes);
                String text = new String(bytes);
                _numberField.setText(text);
            } catch (IOException ex)
            {
                Toast.makeText(this, "Ошибка при чтении", Toast.LENGTH_SHORT).show();
            }
        }
    }
}