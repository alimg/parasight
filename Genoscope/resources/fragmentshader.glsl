uniform sampler2D baseImage;
uniform vec2 baseSize;

void main()
{
    //vec3 luminanceVector = vec3(0.2125, 0.7154, 0.0721);
    vec2 t=vec2(gl_TexCoord[0]);
    vec4 s = texture2D(baseImage, t);

    vec4 ss[8];
    float cy=1.0/baseSize.y;
    float cx=1.0/baseSize.x;
/*
    ss[0]= texture2D(baseImage, t+vec2(0,cy));
    ss[1]= texture2D(baseImage, t+vec2(0,-cy));
    ss[2]= texture2D(baseImage, t+vec2(cx,0));
    ss[3]= texture2D(baseImage, t+vec2(-cx,0));*/
    ss[0]= texture2D(baseImage, t+vec2(-cx,cy));
    ss[1]= texture2D(baseImage, t+vec2(-cx,-cy));
    ss[2]= texture2D(baseImage, t+vec2(cx,cy));
    ss[3]= texture2D(baseImage, t+vec2(cx,-cy));
//    float mn=1.0,d=0.0,mx=-1.0;
    int i;
//    int j=0,k=0;
    for(i=0;i<4;i++)
	{
		/*d=dot(s,ss[i]);
		if(d<mn)
		{
			mn=d;
	    j=i;
		  }
		if(d>mx)
		{
			mx=d;k=i;
		}*/
		s+=vec4(ss[i].rgb*ss[i].a,ss[i].a);
    }
    //s-=vec4(ss[j].rgb*ss[j].a,ss[j].a);
    //s-=vec4(ss[k].rgb*ss[k].a,ss[k].a);
    s/=4.0;

    gl_FragColor = s;
    
}
