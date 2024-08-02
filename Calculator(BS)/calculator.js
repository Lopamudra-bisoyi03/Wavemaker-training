document.addEventListener('DOMContentLoaded', function() {
    const expressionDisplay = document.getElementById('expressionDisplay');
    const resultDisplay = document.getElementById('resultDisplay');
    let currentInput = '';
    let memory = 0;

    function updateDisplays(expression, result) {
        expressionDisplay.value = expression;
        resultDisplay.value = result;
    }

    function handleButtonClick(event) {
        const buttonText = event.target.innerText;

        if (!isNaN(buttonText) || buttonText === '.') { 
            currentInput += buttonText;
            updateDisplays(currentInput, resultDisplay.value);
        } else {
            switch (buttonText) {
                case 'C':
                    currentInput = '';
                    updateDisplays('', '');
                    break;
                case 'CE':
                    currentInput = currentInput.slice(0, -1);
                    updateDisplays(currentInput, resultDisplay.value);
                    break;
                case '←':
                    const cursorPosition = expressionDisplay.selectionStart; 
                    if (cursorPosition > 0) {
                        expressionDisplay.setSelectionRange(cursorPosition - 1, cursorPosition - 1);
                    }
                    break;
                case '±':
                    currentInput = (parseFloat(currentInput) * -1).toString();
                    updateDisplays(currentInput, resultDisplay.value);
                    break;
                case '√':
                    currentInput = Math.sqrt(parseFloat(currentInput)).toString();
                    updateDisplays(currentInput, resultDisplay.value);
                    break;
                case '1/x':
                    currentInput = (1 / parseFloat(currentInput)).toString();
                    updateDisplays(currentInput, resultDisplay.value);
                    break;
                case '=':
                    try {
                        const result = eval(currentInput).toString();
                        updateDisplays(currentInput, result);
                        currentInput = result; 
                    } catch {
                        updateDisplays(currentInput, 'Error');
                        currentInput = ''; 
                    }
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                    currentInput += buttonText;
                    updateDisplays(currentInput, resultDisplay.value);
                    break;
                case '%':
                    currentInput = (parseFloat(currentInput) / 100).toString();
                    updateDisplays(currentInput, resultDisplay.value);
                    break;
                case 'MC':
                    memory = 0;
                    console.log('Memory cleared:', memory); 
                    break;
                case 'MR':
                    currentInput = memory.toString();
                    updateDisplays(currentInput, resultDisplay.value);
                    console.log('Memory recalled:', memory); 
                    break;
                case 'MS':
                    memory = parseFloat(currentInput);
                    currentInput = ''; 
                    updateDisplays('', '');
                    console.log('Memory stored:', memory); 
                    break;
                case 'M+':
                        if (currentInput !== '') {
                            memory += parseFloat(currentInput);
                        }
                        currentInput = memory.toString();  
                        updateDisplays(null,currentInput); 
                        console.log('Memory added:', memory);
                        break;
                case 'M-':
                    if (currentInput !== '') {
                        memory -= parseFloat(currentInput);
                    }
                    currentInput = memory.toString();  
                    updateDisplays(null,currentInput); 
                    console.log('Memory subtracted:', memory);
                    break;
            }
        }
    }

    document.querySelectorAll('button').forEach(button => {
        button.addEventListener('click', handleButtonClick);
    });
});
