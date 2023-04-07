import { Component, Input, OnInit } from '@angular/core';
import { Bookmark } from '../dto/bookmark';

@Component({
  selector: 'app-bookmark',
  templateUrl: './bookmark.component.html',
  styleUrls: ['./bookmark.component.css']
})
export class BookmarkComponent implements OnInit {
  @Input('bookmark') bookmark?: Bookmark;

  constructor() { }

  ngOnInit(): void {
  }

}
