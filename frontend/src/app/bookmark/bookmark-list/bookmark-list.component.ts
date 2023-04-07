import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
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

  constructor(private bookmarkService: BookmarkService) {
  }

  ngOnInit(): void {
    this.bookmarkService.getAllBookmarks().subscribe({
      next: (response: Bookmark[]) => this.onResponse(response),
      error: (error: HttpErrorResponse) => this.onError(error)
    })
  }

  onResponse(response: Bookmark[]): void {
    this.bookmarks = response;
  }

  onError(error: HttpErrorResponse): void {
    this.isError = true;
    this.errorMsg = error.error.message;
  }
}
