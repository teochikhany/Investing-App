import { Directive } from '@angular/core';
import { AbstractControl, Validator, NG_VALIDATORS, ValidationErrors } from '@angular/forms';

@Directive({
    selector: '[appPriceValidator]',
    providers: [{
        provide: NG_VALIDATORS,
        useExisting: PriceValidatorDirective,
        multi: true
    }]
})
export class PriceValidatorDirective implements Validator {

    constructor() { }

    validate(control: AbstractControl): ValidationErrors | null {

        if (control.value > 10) {
            return null
        }
        else {
            return { 'priceInvalid': true }
        }
    }

}
