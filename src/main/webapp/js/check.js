let xValid = false, yValid = false, rValid = false;
const xValidValues = [-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2];
const rValidValues = [1, 1.5, 2, 2.5, 3];


function validateSelection(value, validValues) {
    return validValues.includes(value);
}


let selectedXBtn;
const errorMessageBox = document.getElementById('error-message');
document.addEventListener("DOMContentLoaded", function () {
    const xBtns = document.querySelectorAll('.button-group__button');

    xBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            const selectedValue = parseFloat(btn.value);
            xBtns.forEach(otherBtn => {
                otherBtn.classList.remove('active');
            });
            if (selectedValue !== selectedXBtn && selectedValue.toString() === btn.value && validateSelection(selectedValue, xValidValues)) {
                btn.classList.add('active');
                selectedXBtn = selectedValue;
                xValid = true;
                errorMessageBox.textContent = '';
            } else {
                btn.classList.remove('active');
                selectedXBtn = undefined;
                xValid = false;
                errorMessageBox.textContent = 'Check the value.';
            }
            toggleSubmitBtn();
        });
    });
});


const yInput = document.querySelector('input[name="y"]');
yInput.addEventListener('input', () => {
    yValid = false;

    const regex = /^[0-9.,-]+$/;
    if (!regex.test(yInput.value)) {
        yInput.setCustomValidity('Check the value.');
        yInput.reportValidity();
        toggleSubmitBtn();
        return;
    }

    const yValue = parseFloat(yInput.value.trim().replace(',', '.'));
    if (isNaN(yValue)) {
        yInput.setCustomValidity('Check the value.');
    } else if (yValue < -3 || yValue > 3) {
        yInput.setCustomValidity('The value must be in the interval (-3 ... 3).');
    } else {
        yValid = true;
        yInput.setCustomValidity('');
    }
    yInput.reportValidity();
    toggleSubmitBtn();
});


const rRadios = document.querySelectorAll('input[name="r"]');
rRadios.forEach(rRadio => {
    rRadio.addEventListener('change', () => {
        const selectedValue = parseFloat(rRadio.value);
        if (validateSelection(selectedValue, rValidValues) && selectedValue.toString() === rRadio.value) {
            rValid = true;
            rRadio.setCustomValidity('');
        } else {
            rValid = false;
            rRadio.setCustomValidity('Check the value.');
        }
        rRadio.reportValidity();
        toggleSubmitBtn();
    });
});


const submitBtn = document.querySelector('[type="submit"]');
function toggleSubmitBtn() {
    submitBtn.disabled = !(xValid && yValid && rValid)
}