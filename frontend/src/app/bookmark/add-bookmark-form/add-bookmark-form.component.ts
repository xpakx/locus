import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BookmarkService } from '../bookmark.service';
import { Bookmark } from '../dto/bookmark';
import { BookmarkForm } from '../forms/bookmark-form';

@Component({
  selector: 'app-add-bookmark-form',
  templateUrl: './add-bookmark-form.component.html',
  styleUrls: ['./add-bookmark-form.component.css']
})
export class AddBookmarkFormComponent implements OnInit {
  form: FormGroup<BookmarkForm>;
  isError: boolean = false;
  errorMsg: String = "";

  constructor(private fb: FormBuilder, private bookmarkService: BookmarkService) {
    this.form = this.fb.nonNullable.group({
      url: ["", [Validators.required, Validators.minLength(1)]]
    });
  }

  ngOnInit(): void {
  }

  onSubmit() {
    if(this.form.invalid) {
      return;
    }
    this.bookmarkService.bookmarkPage({url: this.form.controls.url.value}).subscribe({
      next: (response: Bookmark) => this.onCreation(response),
      error: (error: HttpErrorResponse) => this.onError(error)
    })
  }

  onCreation(response: Bookmark): void {
    //TODO
  }

  onError(error: HttpErrorResponse): void {
    this.isError = true;
    this.errorMsg = error.error.message;
  }
}
