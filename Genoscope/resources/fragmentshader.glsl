uniform sampler2D baseImage;
uniform vec2 baseSize;

void main()
{
    vec3 luminanceVector = vec3(0.2125, 0.7154, 0.0721);
    vec2 t=vec2(gl_TexCoord[0]);
    vec4 sample = texture2D(baseImage, t);

    vec4 sample1 = texture2D(baseImage, t+vec2(0,1.0/baseSize.y));
    sample1 += texture2D(baseImage, t+vec2(0,-1.0/baseSize.y));
    sample1 += texture2D(baseImage, t+vec2(1.0/baseSize.x,0));
    sample1 += texture2D(baseImage, t+vec2(-1.0/baseSize.x,0));
    sample.rgb= (sample.rgb+sample1.rgb)/5.0;

    gl_FragColor = sample;
    
}
