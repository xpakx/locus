import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { BookmarkService } from '../bookmark.service';
import { Bookmark } from '../dto/bookmark';

@Component({
  selector: 'app-bookmark-list',
  templateUrl: './bookmark-list.component.html',
  styleUrls: ['./bookmark-list.component.css']
})
export class BookmarkListComponent implements OnInit {
  isError: boolean = false;
  errorMsg: String = "";
  bookmarks: Bookmark[] = [];
  init: boolean = false;

  constructor(private bookmarkService: BookmarkService) {
  }

  ngOnInit(): void {
    this.getAllBookmarks();
  }

  private getAllBookmarks() {
    this.bookmarkService.getAllBookmarks().subscribe({
      next: (response: Bookmark[]) => this.onResponse(response),
      error: (error: HttpErrorResponse) => this.onError(error)
    });
  }

  private searchForBookmarks(searchString: string) {
    this.bookmarkService.searchBookmarks(searchString).subscribe({
      next: (response: Bookmark[]) => this.onResponse(response),
      error: (error: HttpErrorResponse) => this.onError(error)
    });
  }

  @Input('search') set searchString(value: string) {
    if(!this.init) {
      return;
    }
    if(value == '') {
      this.getAllBookmarks();
    } else {
      this.searchForBookmarks(value);
    }
  }

  onResponse(response: Bookmark[]): void {
    this.init = true;
    this.bookmarks = response;
  }

  onError(error: HttpErrorResponse): void {
    this.isError = true;
    this.errorMsg = error.error.message;
  }
}
