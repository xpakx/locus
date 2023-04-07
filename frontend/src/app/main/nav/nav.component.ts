import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SearchForm } from 'src/app/bookmark/forms/search-form';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {
  @Output("searchAction") search = new EventEmitter<string>();
  form: FormGroup<SearchForm>;
  isError: boolean = false;
  errorMsg: String = "";

  constructor(private fb: FormBuilder) {
    this.form = this.fb.nonNullable.group({
      value: ["", [Validators.required, Validators.minLength(1)]]
    });
  }

  ngOnInit(): void {
  }

  onSubmit() {
    if(this.form.invalid) {
      return;
    }

    this.search.emit(this.form.controls.value.value)
  }
}
