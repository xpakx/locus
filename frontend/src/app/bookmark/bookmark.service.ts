import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { JwtService } from '../common/jwt-service';
import { Bookmark } from './dto/bookmark';
import { BookmarkRequest } from './dto/bookmark-request';

@Injectable({
  providedIn: 'root'
})
export class BookmarkService extends JwtService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { 
    super();
  }

  public bookmarkPage(request: BookmarkRequest): Observable<Bookmark> {
    return this.http.post<Bookmark>(`${this.apiUrl}/bookmarks`, request, { headers: this.getHeaders() });
  }

  public getBookmark(id: number): Observable<Bookmark> {
    return this.http.get<Bookmark>(`${this.apiUrl}/bookmarks/${id}`, { headers: this.getHeaders() });
  }

  public searchBookmarks(searchString: string): Observable<Bookmark[]> {
    let params = new HttpParams().set('searchString', searchString);
    return this.http.get<Bookmark[]>(`${this.apiUrl}/bookmarks/${searchString}`, { headers: this.getHeaders(), params: params });
  }
}
